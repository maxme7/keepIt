package com.example.keepit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
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
//        webViewClient = WebViewClient()
        webView = findViewById(R.id.webView) //view binding?
        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)

        //not needed currently; if page could be laoded successfully
        //https://stackoverflow.com/questions/28385768/android-how-to-check-for-successful-load-of-url-when-using-webview-loadurl
        var failedLoading = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!failedLoading) {
                    Snackbar.make(
                        findViewById(R.id.coordinatorLayout),
                        "page finished loading",
                        1000
                    ).show()
                    editText.setText(webView.url.toString())
                }

                //apply javascript
                //https://stackoverflow.com/questions/19621427/webview-manipulate-dom-after-webpage-has-been-loaded
//                view.loadUrl("javascript:document.body.setAttribute(\"hidden\", true)")

                //including local js and css
                view.loadUrl("javascript:document.head.innerHTML += \"<link href='file:///android_asset/script.js' rel='stylesheet'>\"")

//                view.loadDataWithBaseURL()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                failedLoading = true
            }
        }

        //browser settings
        //https://stackoverflow.com/questions/8298237/video-not-appearing-on-a-webview
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.setSupportMultipleWindows(false)
        settings.setSupportZoom(true)
        settings.allowFileAccess = true

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false

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