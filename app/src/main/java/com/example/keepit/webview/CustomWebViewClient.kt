package com.example.keepit.webview

import android.util.Log
import android.webkit.*
import java.io.IOException
import java.io.InputStream

class CustomWebViewClient(private var scripts: Array<String> = emptyArray(),
                          private var includeFilter: Array<String> = arrayOf(""),
                          private var excludeFilter: Array<String> = emptyArray()) : WebViewClient() {
    var failedLoading = false

    override fun shouldInterceptRequest(webView: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        // only load resources where the resource url contains any string from the provided filter
        if (includeFilter.any { request?.url.toString().contains(it) } && excludeFilter.none { request?.url.toString().contains(it) }) {
            Log.i("LOADED", request?.url.toString())
            return super.shouldInterceptRequest(webView, request)
        } else {
            Log.i("FILTERED", request?.url.toString())
            return WebResourceResponse("text/javascript", "UTF-8", null) //empty web resource response
        }
    }

//    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//        Log.i("LOADED", request?.url.toString())
//        return super.shouldOverrideUrlLoading(view, request)
//    }


    override fun onPageFinished(webView: WebView, url: String) {
        super.onPageFinished(webView, url)

//        Toast.makeText(view.context, "شسيب", Toast.LENGTH_LONG).show()
        if (!failedLoading) { //TODO useless
        }

        Log.i("RE", webView.progress.toString())
        if (webView.progress < 100) return
        //called before progress 100%: https://stackoverflow.com/questions/18282892/android-webview-onpagefinished-called-twice

        //apply javascript
        //https://stackoverflow.com/questions/19621427/webview-manipulate-dom-after-webpage-has-been-loaded


        //=> https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)
        //next reloaded; security implication; injects java object

//        view.loadUrl("javascript: " + fetchAssetFile(view, "addStars.js"))

        executeScripts(webView, scripts)
    }

    fun executeScripts(webView: WebView, scripts: Array<String>) {
        scripts.forEach { webView.evaluateJavascript(fetchAssetFile(webView, it), null) }
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


/**
 *  fileName - relative file name from the file:///android_asset/
 *  returns whole file
 */
fun fetchAssetFile(view: WebView, fileName: String): String {
    var file: String = "" //TODO should empty string be returned by default?
    var inputStream: InputStream? = null

    try {
        inputStream = view.resources.assets.open(fileName)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        file = String(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }

    return file
}

// shouldInterceptRequest: https://stackoverflow.com/questions/24547446/android-block-ads-in-webview
// block requests: https://joshuatz.com/posts/2021/webview-intercepting-and-blocking-requests/

// read file from assets: https://www.tutorialspoint.com/how-to-read-a-file-from-assets-on-android
// read files: https://www.baeldung.com/kotlin/read-file
// async: https://www.youtube.com/watch?v=t-3TOke8tq8