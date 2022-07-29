package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewClient
import com.example.keepit.webview.CustomWebViewFragment

private const val defaultUrl: String = "https://translate.yandex.com/?lang=it-en"
//private val requestIncludeFilter = arrayOf("")
//private val requestExcludeFilter = emptyArray<String>()
private val scripts = arrayOf("yandex.js", "yandex_addStars.js")

class YandexFragment :  CustomWebViewFragment(defaultUrl, CustomWebViewClient(scripts))
