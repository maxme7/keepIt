package com.example.keepit

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CustomJavascriptInterface(private val webView: WebView, private val injectionObject: InjectionObject) {

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
    fun addEntry(url: String, sourceLang: String, targetLang: String, sourceWord: String, targetWord: String, gram: String?,
                 phon: String?, ind: String?): Boolean {
//        val injectionObject: ArrayList<DictionaryEntryData> = ArrayList()

        Toast.makeText(webView.context, url, Toast.LENGTH_SHORT).show()

        val entry = DictionaryEntryData(
            url,
            Date(),
            Language.valueOf(sourceLang.uppercase()),
            Language.valueOf(targetLang.uppercase()),
            sourceWord,
            targetWord,
            gram,
            phon,
            ind,
            emptyArray()) //TODO categories (?)

        Log.i("ENTRY", entry.toString())
        return true
    }

    @JavascriptInterface @Throws(Exception::class)
    fun performClick(s: String) {
        Toast.makeText(webView.context, s, Toast.LENGTH_SHORT).show()
    }
}

// gson: https://www.bezkoder.com/kotlin-parse-json-gson/#GsontoJson_method