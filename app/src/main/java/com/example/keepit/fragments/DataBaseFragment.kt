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
import com.example.keepit.R
import com.example.keepit.databinding.FragmentDataBaseBinding
import com.example.keepit.enums.Language
import com.example.keepit.models.DataBaseViewModel
import com.example.keepit.BR.dbModel
import com.example.keepit.DictEntryRecyclerViewAdapter

class DataBaseFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var dataBaseViewModel: DataBaseViewModel //by activityViewModels() //shared between fragments
    lateinit var binding: FragmentDataBaseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_base, container, false)
        binding.lifecycleOwner = this


        val srcSpinner = binding.sourceLangSpinner
        srcSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        srcSpinner.onItemSelectedListener = this

        val tarSpinner = binding.targetLangSpinner
        tarSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        tarSpinner.onItemSelectedListener = this

        //listview
        val adapter = DictEntryRecyclerViewAdapter(this, androidx.appcompat.R.layout.abc_popup_menu_header_item_layout, [])
        binding.listView.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBaseViewModel = ViewModelProvider(requireActivity()).get(DataBaseViewModel::class.java)
        binding.setVariable(dbModel, dataBaseViewModel)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sourceLangSpinner -> dataBaseViewModel.setSourceLang(Language.values()[position])
            R.id.targetLangSpinner -> dataBaseViewModel.setTargetLang(Language.values()[position])
            else -> {}
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

}