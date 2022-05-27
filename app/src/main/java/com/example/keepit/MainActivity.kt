package com.example.keepit

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var button: ImageButton
    private lateinit var editText: EditText

    private val bookmarks: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //setup
        webView = findViewById(R.id.webView) //view binding?
        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)

        webView.webViewClient = CustomWebViewClient()
        webView.loadUrl("https://de.langenscheidt.com/deutsch-arabisch/machen")


        //browser settings
        //https://stackoverflow.com/questions/8298237/video-not-appearing-on-a-webview
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.setSupportMultipleWindows(false)
        settings.setSupportZoom(true)
        settings.allowFileAccess = true
        settings.loadWithOverviewMode = true

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        WebView.setWebContentsDebuggingEnabled(true)

        //search button
        button.setOnClickListener {
            Snackbar.make(findViewById(R.id.coordinatorLayout), editText.text.toString(), 1000)
                .show()
            webView.loadUrl(editText.text.toString())
            webView
        }

        //search on enter key
        //https://stackoverflow.com/questions/31378621/keylistener-on-edittext-android
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                button.performClick()
            }
            true
        }

        //https://stackoverflow.com/questions/33698122/android-change-actionbar-title-text-color
        val s = SpannableString(title)
        s.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.dark_red)),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar?.title = s
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu);
        return true;
    }

    //https://developer.android.com/training/appbar/actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.bookmarkButton -> {
//            val s = editText.text.toString()
            val s = webView.url.toString()
            if (!bookmarks.contains(s)) {
                bookmarks.add(s)
            }
            true
        }
        R.id.backButton -> {
            webView.goBack()
            true
        }
        R.id.forwardButton -> {
            webView.goForward()
            true
        }
        R.id.bookarmsButton -> {
            showDialog()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    //https://protocoderspoint.com/android-alert-dialog-box-with-a-list-of-options/
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bookmarks")
        builder.setItems(bookmarks.toTypedArray()) { dialog, which ->
            editText.setText(bookmarks[which])
            button.performClick()
            dialog.dismiss()
        }

        builder.create().show()
    }
}