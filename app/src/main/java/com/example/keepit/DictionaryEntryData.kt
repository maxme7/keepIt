package com.example.keepit

import java.util.*

data class DictionaryEntryData(
    var url: String,
    var date: Date,
    var sourceLang: Language,
    var targetLang: Language,
    var sourceWord: String,
    var targetWord: String,

    // TODO potentially rename; just adopted from langenscheidt
    var gram: String?,
    var phon: String?,
    var ind: String?,

    var categories: Array<String>
) {}


enum class Language(val fullName: String) {
    DE("german"),
    EN("english"),
    AR("arabic");

    companion object {
        fun findByFullName(fullName: String): Language? {
            return when (fullName) {
                "german" -> DE
                "english" -> EN
                "arabic" -> AR
                else -> null
            }
        }
    }
}