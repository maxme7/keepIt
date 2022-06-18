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
import com.example.keepit.databinding.FragmentTestBinding

class testFragment : Fragment() {

    private lateinit var dataBaseViewModel: DataBaseViewModel //by activityViewModels() //shared between fragments
    lateinit var binding: FragmentTestBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        binding.lifecycleOwner = this



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBaseViewModel = ViewModelProvider(requireActivity()).get(DataBaseViewModel::class.java)
        binding.setVariable(dbModel, dataBaseViewModel)
    }


}

