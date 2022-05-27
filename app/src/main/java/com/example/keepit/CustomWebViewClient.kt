package com.example.keepit

import android.util.Log
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class CustomWebViewClient : WebViewClient() {
    var failedLoading = false

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {

        //Currently only for langenscheidt; filters cookies and add banner
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
//                view.loadUrl("javascript:document.body.setAttribute(\"hidden\", true)")


        //https://stackoverflow.com/questions/39831360/webview-detect-button-click-event
        //https://www.youtube.com/watch?v=9RwJeocTgJg
        view.addJavascriptInterface(object : Any() {
            @JavascriptInterface @Throws(Exception::class)
            fun performClick(s: String) {
                Toast.makeText(view.context, s, Toast.LENGTH_LONG).show()
            }
        }, "enter")
        //enter refers to the global window object which has a method PerformClick() that can be invoked
        //WERBUNG !!! weg!

        //=> https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)
        //next reloaded; security implication; injects java object


        //including local js and css
        fun sdf() {
            view.loadUrl("javascript:console.log(\"hello\");\n" +
                    "setTimeout(function(){if(document.querySelector(\"#onetrust-pc-btn-handler\")) document.querySelector(\"#onetrust-pc-btn-handler\").click()}, 0);\n" +
                    "setTimeout(function(){if(document.querySelector(\".save-preference-btn-handler\")) document.querySelector(\".save-preference-btn-handler\").click()}, 0);\n" +
                    "document.getElementById('search-string').addEventListener('keydown', ()=>enter.performClick('pass some string value'));")
        }
//        sdf()
        //directly pasing script works. including a js file or alternatively loading file content and passing that to .loadUrl might be trickier
        //view.loadUrl("javascript:document.head.innerHTML += \"<script src='file:///android_asset/script.js' type='text/javascript' defer/>\"")

        //SEMI COLONS IMPORTANT !!

        fun addStars() {
            view.loadUrl(("javascript:" +
                    "let el = document.querySelectorAll(\".lemma-group .lemma-entry .col1 .lemma-pieces\");\n" +
                    "for(let e of el){\n" +
                    "let b = document.createElement(\"img\");\n" +
                    "b.src=\"https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg\";\n" +
                    "b.classList.add(\"text-to-speech\");\n" +
                    "b.width=\"20\";\n" +
                    "b.height=\"20\";\n" +
                    "e.appendChild(b);\n" +
                    "}" +
                    "").trimMargin().trimIndent())


        }
//        let el = document.querySelectorAll(".lemma-group .lemma-entry .col1 .lemma-pieces")
//        for(let e of el){
//            let b = document.createElement("span")
//            b.textContent="https://www.reshot.com/preview-assets/icons/GC6DYT5UXL/star-GC6DYT5UXL.svg";
//            b.classList.add("text-to-speech")
//            b.width="20"
//            b.height="20"
//            e.appendChild(b)
//        }
        addStars()

//        view.loadUrl("file:///android_asset/star.svg")

        //view.loadDataWithBaseURL()
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

// shouldInterceptRequest: https://stackoverflow.com/questions/24547446/android-block-ads-in-webview
// block requests: https://joshuatz.com/posts/2021/webview-intercepting-and-blocking-requests/