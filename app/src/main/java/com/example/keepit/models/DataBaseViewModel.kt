package com.example.keepit.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepit.enums.Language

class DataBaseViewModel : ViewModel() {

    //TODO how to use livedata with non-primitive types

    private lateinit var sourceLang: Language
    val sourceLangString = MutableLiveData<String>() //to use in xml layout

    private lateinit var targetLang: Language
    val targetLangString = MutableLiveData<String>()

    fun setSourceLang(lang: Language) {
        sourceLang = lang
        sourceLangString.value = lang.toString()
    }

    fun getSourceLang(): Language {
        return sourceLang
    }

    fun setTargetLang(lang: Language) {
        targetLang = lang
        targetLangString.value = lang.toString()
    }

    fun getTargetLang(): Language {
        return targetLang
    }
}


// view model tutorial: https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel#3 ...not working