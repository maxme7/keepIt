package com.example.keepit

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class CustomJavascriptInterface(private val context: Context, private val injectionObject: InjectionObject ) {

    private val gson = Gson()

    @JavascriptInterface @Throws(Exception::class)
    fun getField(): String {
        return "entries[0].targetLang.completeName"
    }

    /**
     * object to inject data into webView
     */
    @JavascriptInterface @Throws(Exception::class)
    fun getInjectionObject(): String {
        return gson.toJson(injectionObject)
    }

    @JavascriptInterface @Throws(Exception::class)
    fun addEntry(sourceLang: String,targetLang: Language,sourceWord: String, targetWord: String): Boolean {
//        val injectionObject: ArrayList<DictionaryEntryData> = ArrayList()

//        > fetch url (via view..)
//        > parse enum from string
//        > get fields from website and feed it to addEntry

        val entry = DictionaryEntryData("http://", Date(), Language.DE, Language.EN, "Kuh", "Cow", emptyArray())
        Log.i("ENTRY", entry.toString())
        return true
    }

    @JavascriptInterface @Throws(Exception::class)
    fun performClick(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }
}

// gson: https://www.bezkoder.com/kotlin-parse-json-gson/#GsontoJson_method