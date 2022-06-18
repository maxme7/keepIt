package com.example.keepit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.keepit.R
import com.example.keepit.databinding.FragmentDataBaseBinding
import com.example.keepit.enums.Language
import com.example.keepit.models.DataBaseViewModel
import com.example.keepit.BR.dbModel

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