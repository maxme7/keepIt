package com.example.keepit

import java.util.*

data class DictionaryEntryData(
    var url: String,
    var date: Date,
    var sourceLang: Language,
    var targetLang: Language,
    var sourceWord: String,
    var targetWord: String,
    var categories: Array<String>
) {}


enum class Language(val completeName: String) {
    DE("german"),
    EN("english"),
    AR("arabic")
}