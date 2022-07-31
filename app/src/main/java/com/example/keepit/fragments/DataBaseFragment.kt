package com.example.keepit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.keepit.R
import com.example.keepit.databinding.FragmentDataBaseBinding
import com.example.keepit.enums.Language
import com.example.keepit.models.DataBaseViewModel
import com.example.keepit.BR.dbModel
import com.example.keepit.DictEntryRecyclerViewAdapter
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.getDb
import kotlinx.coroutines.runBlocking

class DataBaseFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var dataBaseViewModel: DataBaseViewModel //by activityViewModels() //shared between fragments
    lateinit var binding: FragmentDataBaseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_base, container, false)
        binding.lifecycleOwner = this


        val srcSpinner = binding.sourceLangSpinner
        srcSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        srcSpinner.setSelection(Language.DE.ordinal) //initial
        srcSpinner.onItemSelectedListener = this

        val tarSpinner = binding.targetLangSpinner
        tarSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        tarSpinner.setSelection(Language.AR.ordinal)
        tarSpinner.onItemSelectedListener = this

        binding.swappButton.setOnClickListener {
            swappLanguages()
        }

        //listview
        populateRecyclerView(Language.findByFullName(srcSpinner.selectedItem.toString())!!,
            Language.findByFullName(tarSpinner.selectedItem.toString())!!)

        return binding.root
    }

    fun populateRecyclerView(srcLang: Language, targLang: Language) {
        runBlocking {
            val dictEntryDao = getDb(requireContext()).dictEntryDao()

            val adapter = DictEntryRecyclerViewAdapter(requireContext(), dictEntryDao.getEntriesByLang(srcLang, targLang))
            binding.listView.adapter = adapter
            binding.listView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBaseViewModel = ViewModelProvider(requireActivity()).get(DataBaseViewModel::class.java)
        binding.setVariable(dbModel, dataBaseViewModel)

        //initializing
        dataBaseViewModel.setSourceLang(Language.DE)
        dataBaseViewModel.setTargetLang(Language.AR)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sourceLangSpinner -> dataBaseViewModel.setSourceLang(Language.values()[position])
            R.id.targetLangSpinner -> dataBaseViewModel.setTargetLang(Language.values()[position])
            else -> {}
        }

        populateRecyclerView(dataBaseViewModel.getSourceLang(), dataBaseViewModel.getTargetLang())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    fun swappLanguages() {
        val sourceLang = dataBaseViewModel.getSourceLang()
        dataBaseViewModel.setSourceLang(dataBaseViewModel.getTargetLang())
        dataBaseViewModel.setTargetLang(sourceLang)

        binding.sourceLangSpinner.setSelection(Language.values().indexOf(dataBaseViewModel.getSourceLang()))
        binding.targetLangSpinner.setSelection(Language.values().indexOf(dataBaseViewModel.getTargetLang()))

        populateRecyclerView(dataBaseViewModel.getSourceLang(), dataBaseViewModel.getTargetLang())

    }

}