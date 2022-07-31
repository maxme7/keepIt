package com.example.keepit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.keepit.DictEntryCollectionRecyclerViewAdapter
import com.example.keepit.R
import com.example.keepit.databinding.FragmentCategorySortingBinding
import com.example.keepit.enums.Language
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.entities.Collection
import kotlinx.coroutines.runBlocking

class CategorySortingFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCategorySortingBinding? = null
    private val binding get() = _binding!!

    private var spinnerSourceLang: Language = Language.DE
    private var spinnerTargetLang: Language = Language.AR
    private var showUnassigned: Boolean = true;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCategorySortingBinding.inflate(inflater, container, false)
        val view = binding.root


        val srcSpinner = binding.sourceLangSpinner
        srcSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, Language.values())
        srcSpinner.setSelection(spinnerSourceLang.ordinal) //initial
        srcSpinner.onItemSelectedListener = this

        val tarSpinner = binding.targetLangSpinner
        tarSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, Language.values())
        tarSpinner.setSelection(spinnerTargetLang.ordinal)
        tarSpinner.onItemSelectedListener = this

        binding.swappButton.setOnClickListener {
            swappLanguages()
        }

        binding.fab.setOnClickListener {
            showCreateCategoryDialog()
        }

        return view
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
                    val db = Room.databaseBuilder(requireContext().applicationContext, AppDatabase::class.java, "db").build()

                    val collectionDao = db.collectionDao()
                    collectionDao.insertAll(collection)

                    for (v in collectionDao.getAll()) {
                        Log.i("ROOM", "${v.id}  ${v.name}  ${v.sourceLang}  ${v.targetLang}")
                    }
                }
                //TODO update list spinner

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    fun populateRecyclerView(srcLang: Language, targLang: Language) {
        runBlocking {
            val db = Room.databaseBuilder(requireContext().applicationContext, AppDatabase::class.java, "db").build()

            val adapter = if (showUnassigned) {
                val collectionDao = db.collectionDao()
                Log.i("ROOM", collectionDao.getUnassignedDictEntries(srcLang, targLang).toString())
                DictEntryCollectionRecyclerViewAdapter(requireContext(), collectionDao.getUnassignedDictEntries(srcLang, targLang))
            } else {
                val dictEntryDao = db.dictEntryDao()
                Log.i("ROOM",  dictEntryDao.getEntriesByLang(srcLang, targLang).toString())
                DictEntryCollectionRecyclerViewAdapter(requireContext(), dictEntryDao.getEntriesByLang(srcLang, targLang))
            }
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sourceLangSpinner -> spinnerSourceLang = Language.values()[position]
            R.id.targetLangSpinner -> spinnerTargetLang = Language.values()[position]
            else -> {}
        }

        populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    fun swappLanguages() {
        val sourceLang = spinnerSourceLang
        binding.sourceLangSpinner.setSelection(Language.values().indexOf(spinnerTargetLang))
        binding.targetLangSpinner.setSelection(Language.values().indexOf(sourceLang))

        populateRecyclerView(spinnerSourceLang, spinnerTargetLang)
    }

}

