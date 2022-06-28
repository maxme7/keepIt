package com.example.keepit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.keepit.webview.CustomJavascriptInterface
import com.example.keepit.webview.CustomWebViewClient
import com.example.keepit.webview.InjectionObject
import com.example.keepit.R

private const val defaultUrl: String = "https://de.wiktionary.org/wiki"
//private const val defaultUrl: String = "https://de.langenscheidt.com/deutsch-englisch/bow"

class WiktionaryFragment : Fragment() {
    private lateinit var webView: WebView
    private var url: String? = null
    private var scrollY: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragm = inflater.inflate(R.layout.fragment_web_view, container, false)

        //setup
        webView = fragm.findViewById(R.id.webView) //view binding?
        webView.webViewClient = WebViewClient()//CustomWebViewClient()


        Log.i("WEB", url.toString())
//        webView.loadUrl(url ?: "https://de.langenscheidt.com/deutsch-arabisch/gehen")

        //browser settings
        //https://stackoverflow.com/questions/8298237/video-not-appearing-on-a-webview
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.setSupportMultipleWindows(false)
        settings.allowFileAccess = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        WebView.setWebContentsDebuggingEnabled(true)


        //https://stackoverflow.com/questions/39831360/webview-detect-button-click-event
        //https://www.youtube.com/watch?v=9RwJeocTgJg

        val injectionObject = InjectionObject()
        webView.addJavascriptInterface(CustomJavascriptInterface(activity, webView, injectionObject), "android") //TODO companion object in jsInterface?
        //second parameter refers to the global window object which has a method PerformClick() that can be invoked


        //search on enter key
        //https://stackoverflow.com/questions/31378621/keylistener-on-edittext-android
//        editText.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                button.performClick()
//            }
//            true
//        }

        return fragm
    }

    override fun onResume() {
        super.onResume()

        //load restored url after state change or default url
        webView.loadUrl(url ?: defaultUrl)
        webView.scrollTo(0, scrollY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", webView.url)
        outState.putInt("scrollY", webView.scrollY) //not perfect, but better. different layouts do not match with scrollY TODO
    }

    // restored after onStart() and before onResume()
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        url = savedInstanceState?.getString("url")
        scrollY = savedInstanceState?.getInt("scrollY") ?: 0
    }

    //TODO also restore on activity level?? -> problem when switching screen mode

}