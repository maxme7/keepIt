package com.example.keepit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepit.DictEntryCollectionRecyclerViewAdapter
import com.example.keepit.OnItemCheckListener
import com.example.keepit.R
import com.example.keepit.databinding.FragmentCategorySortingBinding
import com.example.keepit.enums.Language
import com.example.keepit.room.entities.Collection
import com.example.keepit.room.entities.DictEntry
import com.example.keepit.room.entities.DictEntryCollection
import com.example.keepit.room.getDb
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates


class CollectionSortingFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCategorySortingBinding? = null
    private val binding get() = _binding!!

    private var spinnerSourceLang: Language = Language.DE
    private var spinnerTargetLang: Language = Language.AR
    private var showUnassigned: Boolean = false

    private var selectedCollectionIndex by Delegates.notNull<Int>()
    private val currentSelectedItems: ArrayList<DictEntry> = ArrayList()
    private lateinit var collectionList: List<Collection>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCategorySortingBinding.inflate(inflater, container, false)
        val view = binding.root


        val srcSpinner = binding.sourceLangSpinner
        srcSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, Language.values())
        srcSpinner.setSelection(spinnerSourceLang.ordinal, true) //initial
        srcSpinner.onItemSelectedListener = this

        val tarSpinner = binding.targetLangSpinner
        tarSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, Language.values())
        tarSpinner.setSelection(spinnerTargetLang.ordinal, true)
        tarSpinner.onItemSelectedListener = this

        val selectSpinner = binding.selectionSpinner
        selectSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, getCollectionNames())
        selectSpinner.onItemSelectedListener = this

        binding.swappButton.setOnClickListener {
            swappLanguages()
        }

        binding.fab.setOnClickListener {
            showCreateCategoryDialog()
        }

        binding.addToCollectionButton.setOnClickListener {
            lifecycleScope.launch {
                val decDao = getDb(requireContext().applicationContext).dictEntryCollectionDao()

                for (item in currentSelectedItems) {
                    val association = DictEntryCollection(item.id, collectionList[selectedCollectionIndex].id)
                    decDao.insertAll(association)
                }

                reloadSelectionSpinner(requireView())
                populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
            }
        }

        binding.unassignedSwitch.setOnClickListener {
            val switch = it as SwitchCompat
            if (switch.isChecked) {
                switch.text = "all"
                showUnassigned = false
            } else {
                switch.text = "unassigned"
                showUnassigned = true
            }

            reloadSelectionSpinner(requireView())
            populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        //TODO why the hell is onResume not called when changing back from langenscheidt fragment? and thus updating list
        populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
    }

    fun reloadSelectionSpinner(view: View) {
        binding.selectionSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, getCollectionNames())
    }

    fun getCollections(): List<Collection> {
        var collections: List<Collection>
        runBlocking { //TODO how to handle return values from coroutine?
            val collectionDao = getDb(requireContext().applicationContext).collectionDao()
            collections = collectionDao.getCollectionByLang(spinnerSourceLang, spinnerTargetLang)
        }
        return collections
    }

    fun getCollectionNames(): List<String> {
        collectionList = getCollections()
        return collectionList.map { it.name }
    }

    private fun showCreateCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("create collection")
        builder.setIcon(R.drawable.ic_baseline_create_24)
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_new_category, null)
        builder.setView(view)
            .setPositiveButton("Create") { dialog, id ->
                val editText = view.findViewById<EditText>(R.id.editTextCollectionName)
                val collectionName = editText?.text.toString()

                val collection = Collection(collectionName, spinnerSourceLang, spinnerTargetLang)

                runBlocking {
                    val collectionDao = getDb(requireContext().applicationContext).collectionDao()
                    collectionDao.insertAll(collection)

                    for (v in collectionDao.getAll()) {
                        Log.i("ROOM", "${v.id}  ${v.name}  ${v.sourceLang}  ${v.targetLang}")
                    }

                    reloadSelectionSpinner(requireView())
                    binding.selectionSpinner.setSelection(binding.selectionSpinner.adapter.count - 1) //select newly created item
                }
                //TODO update list spinner

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    class ItemCheckListener(var itemList: ArrayList<DictEntry>) : OnItemCheckListener {
        override fun onItemCheck(item: DictEntry?) {
            if (item != null) {
                itemList.add(item)
            }
        }

        override fun onItemUncheck(item: DictEntry?) {
            itemList.remove(item)
        }
    }

    private fun populateRecyclerView(srcLang: Language, targLang: Language) {
        lifecycleScope.launch {
            val db = getDb(requireContext().applicationContext)

            val adapter = if (showUnassigned) {
                val collectionDao = db.collectionDao()
                Log.i("ROOM", collectionDao.getUnassignedDictEntries(srcLang, targLang).toString())
                Log.i("ROOM", db.dictEntryDao().getAll().toString())
                Log.i("ROOM", db.dictEntryCollectionDao().getAll().toString())
                Log.i("ROOM", db.collectionDao().getAll().toString())
                DictEntryCollectionRecyclerViewAdapter(requireContext(), collectionDao.getUnassignedDictEntries(srcLang, targLang),
                    ItemCheckListener(currentSelectedItems))
            } else {
                val dictEntryDao = db.dictEntryDao()
                Log.i("ROOM", dictEntryDao.getEntriesByLang(srcLang, targLang).toString())
                DictEntryCollectionRecyclerViewAdapter(requireContext(), dictEntryDao.getEntriesByLang(srcLang, targLang),
                    ItemCheckListener(currentSelectedItems)
                )
            }

            currentSelectedItems.clear()
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var langHasChnaged = false
        when (parent?.id) {
            R.id.sourceLangSpinner -> {
                spinnerSourceLang = Language.values()[position]
                langHasChnaged = true
            }
            R.id.targetLangSpinner -> {
                spinnerTargetLang = Language.values()[position]
                langHasChnaged = true
            }
            R.id.selectionSpinner -> {
                selectedCollectionIndex = position
            }
            else -> {}
        }

        if (langHasChnaged) {
            reloadSelectionSpinner(requireView())
            populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    fun swappLanguages() {
        val sourceLang = spinnerSourceLang
        binding.sourceLangSpinner.setSelection(Language.values().indexOf(spinnerTargetLang))
        binding.targetLangSpinner.setSelection(Language.values().indexOf(sourceLang))

        reloadSelectionSpinner(requireView())
        populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
    }

}

