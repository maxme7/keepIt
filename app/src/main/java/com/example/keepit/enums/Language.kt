package com.example.keepit.enums

enum class Language(val fullName: String) {
    DE("German"),
    EN("English"),
    AR("Arabic"),
    IT("Italian"),
    FR("French"),
    SP("Spanish");

    override fun toString(): String {
        return fullName
    }

    companion object {
        fun findByFullName(fullName: String): Language? {
            return when (fullName) {
                "German" -> DE
                "English" -> EN
                "Arabic" -> AR
                "Italian" -> IT
                "French" -> FR
                "Spanish" -> SP
                else -> null
            }
        }
    }
}