package com.example.keepit.enums

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