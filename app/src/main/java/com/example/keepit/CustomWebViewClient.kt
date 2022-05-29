package com.example.keepit

import android.util.Log
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream

class CustomWebViewClient : WebViewClient() {
    var failedLoading = false

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {

        //Currently only for langenscheidt; filters cookies and add banner TODO
        if (request?.url.toString().contains("langenscheidt") || request?.url.toString().contains("cloudfront") || request?.url.toString()
                .contains("svg")
        ) {
            Log.i("LOADED", request?.url.toString())
            return super.shouldInterceptRequest(view, request)
        } else {
            Log.i("FILTERED", request?.url.toString())
            return WebResourceResponse("text/javascript", "UTF-8", null)
        }
    }


    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.i("LOADED", request?.url.toString())
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

//        Toast.makeText(view.context, "شسيب", Toast.LENGTH_LONG).show()
        if (!failedLoading) {
        }

        //apply javascript
        //https://stackoverflow.com/questions/19621427/webview-manipulate-dom-after-webpage-has-been-loaded



        //=> https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)
        //next reloaded; security implication; injects java object

//        view.loadUrl("javascript: " + fetchAssetFile(view, "addStars.js"))
        view.evaluateJavascript(fetchAssetFile(view, "playAudioDirectly.js"), null)
        view.evaluateJavascript(fetchAssetFile(view, "addStars.js"), null)

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