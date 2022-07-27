package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewClient
import com.example.keepit.webview.CustomWebViewFragment

//deepl.com not loading properly in webview

private const val defaultUrl: String = "https://www.deepl.com/translator-mobile"
private val scripts = arrayOf("deepl.js", "deepl_addStars.js")

class DeeplFragment : CustomWebViewFragment(defaultUrl, CustomWebViewClient())