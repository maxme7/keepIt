package com.example.keepit.webview

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.example.keepit.MainActivity
import com.example.keepit.enums.Language
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.entities.DictEntry
import com.example.keepit.room.getDb
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.util.*

class CustomJavascriptInterface(private val activity: FragmentActivity?, private val webView: WebView, private val injectionObject: InjectionObject) {



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
    fun entryExists(sourceLang: String?, targetLang: String?, sourceWord: String?, targetWord: String?, gram: String?, phon: String?,
                    ind: String?): Boolean {

        val dictEntries: List<DictEntry> = findEntry(sourceLang, targetLang, sourceWord, targetWord, gram, phon, ind)
        for (e in dictEntries) {
            Log.i("EX",
                "${e.targetWord} ${e.sourceWord}")
        }

        return dictEntries.isNotEmpty()
    }

    fun findEntry(sourceLang: String?, targetLang: String?, sourceWord: String?, targetWord: String?, gram: String?, phon: String?,
                  ind: String?): List<DictEntry> {
        return runBlocking {  //TODO Blocking a problem when db gets bigger?
            val dictEntryDao = getDb(activity!!.applicationContext).dictEntryDao()

            //TODO bottleneck? blocking...  langenscheidt de en gehen or laufen an example where it takes relatively long
            if (sourceLang != null && targetLang != null && sourceWord != null && targetWord != null) {
                return@runBlocking dictEntryDao.find(
                    Language.valueOf(sourceLang.uppercase()),
                    Language.valueOf(targetLang.uppercase()),
                    sourceWord, targetWord,
                    gram.toString(), phon.toString(), ind.toString())
            } else {
                Log.d("JavascriptInterface", "required arguments are null!")
                return@runBlocking emptyList();
            }
        }
    }

    @JavascriptInterface @Throws(Exception::class)
    fun insertEntry(url: String, sourceLang: String, targetLang: String, sourceWord: String, targetWord: String, gram: String?,
                    phon: String?, ind: String?): Boolean {
//        val injectionObject: ArrayList<DictEntry> = ArrayList()

//        Toast.makeText(webView.context, url, Toast.LENGTH_SHORT).show()

        val entry = DictEntry(
            url,
            Date(),
            Language.valueOf(sourceLang.uppercase()),
            Language.valueOf(targetLang.uppercase()),
            sourceWord,
            targetWord,
            gram ?: "null",
            phon ?: "null",
            ind ?: "null",
            emptyArray()) //TODO categories (?)

        Log.i("ENTRY", entry.toString())

        runBlocking {
            val dictEntryDao = getDb(activity!!.applicationContext).dictEntryDao()

            //TODO make sure not inserted twice
            dictEntryDao.insertAll(entry)

            val dictEntries: List<DictEntry> = dictEntryDao.getAll()
            for (e in dictEntries) {
                Log.i("Room",
                    "${e.url} ${e.date} ${e.sourceLang} ${e.targetLang} ${e.sourceWord} ${e.targetWord} ${e.gram} ${e.phon} ${e.ind} ${
                        MainActivity.arrayToString(e.categories)
                    }")
            }
        }

        return true
    }

    @JavascriptInterface @Throws(Exception::class)
    fun deleteEntry(sourceLang: String, targetLang: String, sourceWord: String, targetWord: String, gram: String?,
                    phon: String?, ind: String?): Boolean {

        runBlocking {
            val dictEntryDao = getDb(activity!!.applicationContext).dictEntryDao()

            val entries = findEntry(sourceLang, targetLang, sourceWord, targetWord, gram, phon, ind)
            if (entries.size > 1) Log.d("JavascriptInterface", "found multiple entries where only one was expected!")
            dictEntryDao.delete(entries[0])
        }

        return true
    }

    @JavascriptInterface @Throws(Exception::class)
    fun performClick(s: String) {
        Toast.makeText(webView.context, s, Toast.LENGTH_SHORT).show()
    }
}

// gson: https://www.bezkoder.com/kotlin-parse-json-gson/#GsontoJson_method