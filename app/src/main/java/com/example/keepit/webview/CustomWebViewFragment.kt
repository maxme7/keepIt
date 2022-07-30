package com.example.keepit.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.keepit.R
import com.example.keepit.enums.Language


open class CustomWebViewFragment(private val defaultUrl: String, private val customWebViewClient: WebViewClient = WebViewClient()) : Fragment(),
    AdapterView.OnItemSelectedListener {
    private lateinit var webView: WebView
    private var url: String? = null
    private var scrollY: Int = 0

    private lateinit var topSheet: View
    private var spinnerSourceLang: Language = Language.DE
    private var spinnerTargetLang: Language = Language.AR
    //TODO make automatically change when search in browser changes
    //TODO show user when word to save does not fit to selected group (maybe out open topsheet)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragm = inflater.inflate(R.layout.fragment_web_view, container, false)
        setHasOptionsMenu(true)

        //setup
        webView = fragm.findViewById(R.id.webView) //view binding?
        webView.webViewClient = customWebViewClient//CustomWebViewClient()

        //TODO set fragment title accordingly

        Log.i("WEB", url.toString())
//        webView.loadUrl(url ?: "https://de.langenscheidt.com/deutsch-arabisch/gehen")

        //browser settings
        //https://stackoverflow.com/questions/8298237/video-not-appearing-on-a-webview
        webView.settings.apply {
            javaScriptEnabled = true
            setGeolocationEnabled(false)
            javaScriptCanOpenWindowsAutomatically = false //JavaScript function window.open()

            loadWithOverviewMode = true
            builtInZoomControls = true //pinch gesture to control zooming
            displayZoomControls = false //recommended to disable, since deprecated
            setSupportZoom(true)

            setSupportMultipleWindows(false)
            cacheMode = LOAD_DEFAULT  //alternatives: LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, LOAD_CACHE_ONLY
            allowFileAccess =
                false //enables or disables file system access only. Assets and resources are still accessible using file:///android_asset and file:///android_res.
            allowContentAccess = false //allows WebView to load content from a content provider installed in the system
        }
        //webviewassetloader


        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        WebView.setWebContentsDebuggingEnabled(true)
//        webView.goForward()

        //https://stackoverflow.com/questions/39831360/webview-detect-button-click-event
        //https://www.youtube.com/watch?v=9RwJeocTgJg

        val injectionObject = InjectionObject()
        webView.addJavascriptInterface(CustomJavascriptInterface(activity, webView, injectionObject),
            "android") //TODO companion object in jsInterface?
        //second parameter refers to the global window object which has a method PerformClick() that can be invoked


        //search on enter key
        //https://stackoverflow.com/questions/31378621/keylistener-on-edittext-android
//        editText.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                button.performClick()
//            }
//            true
//        }

        topSheet = fragm.findViewById(R.id.topSheet)
        TopSheetBehavior.from(topSheet).state = TopSheetBehavior.STATE_EXPANDED

        val srcSpinner = fragm.findViewById<Spinner>(R.id.sourceLangSpinner)
        srcSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        srcSpinner.setSelection(Language.DE.ordinal) //initial
        srcSpinner.onItemSelectedListener = this

        val tarSpinner = fragm.findViewById<Spinner>(R.id.targetLangSpinner)
        tarSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, Language.values())
        tarSpinner.setSelection(Language.AR.ordinal)
        tarSpinner.onItemSelectedListener = this


        return fragm
    }

    //https://developer.android.com/training/appbar/actions
    //TODO back and forward navigation (own action bar for webviews?)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.bookmarkButton -> {
////            val s = editText.text.toString()
//            val s = webView.url.toString()
//            if (!bookmarks.contains(s)) {
//                bookmarks.add(s)
//            }
//            true
//        }
        R.id.backButton -> {
            if (webView.canGoBack()) {
                webView.goBack()
            }
            true
        }
        R.id.forwardButton -> {
            if (webView.canGoForward()) {
                webView.goForward()
            }
            true
        }
        R.id.selectGroupButton -> {
            TopSheetBehavior.from(topSheet).state = if (TopSheetBehavior.from(topSheet).state == TopSheetBehavior.STATE_EXPANDED) {
                TopSheetBehavior.STATE_COLLAPSED
            } else {
                TopSheetBehavior.STATE_EXPANDED
            }
            true
        }
        else -> {
            Log.i("WEB", "option menu item selected");

            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        //load restored url after state change or default url
        Log.i("WEB", "defaultURL")
        webView.loadUrl(url ?: defaultUrl)
        webView.scrollTo(0, scrollY)
    }

    override fun onSaveInstanceState(outState: Bundle) { //TODO called when minimizing app but no on if nav drawer change
        super.onSaveInstanceState(outState)

        //saveState()? https://developer.android.com/reference/android/webkit/WebView#saveState(android.os.Bundle)

        if (::webView.isInitialized) {
            outState.putInt("scrollY", webView.scrollY) //not perfect, but better. different layouts do not match with scrollY TODO
            outState.putString("url", webView.url)
        }
    }

    // restored after onStart() and before onResume()
    override fun onViewStateRestored(savedInstanceState: Bundle?) { //TODO called on start and switching back with nav drawer but not if minimize app
        super.onViewStateRestored(savedInstanceState)
        url = savedInstanceState?.getString("url")
        scrollY = savedInstanceState?.getInt("scrollY") ?: 0
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sourceLangSpinner -> spinnerSourceLang = Language.values()[position]
            R.id.targetLangSpinner -> spinnerTargetLang = Language.values()[position]
            else -> {}
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }


    //TODO also restore on activity level?? -> problem when switching screen mode

}