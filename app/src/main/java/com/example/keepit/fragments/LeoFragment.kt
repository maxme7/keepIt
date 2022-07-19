package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewClient
import com.example.keepit.webview.CustomWebViewFragment

private const val defaultUrl: String = "https://dict.leo.org/englisch-deutsch/machen"
private val requestIncludeFilter = arrayOf("dict.leo.org", ".svg")
private val requestExcludeFilter = arrayOf("/adv/")
private val scripts = arrayOf("leo.js", "leo_addStars.js")

class LeoFragment : CustomWebViewFragment(defaultUrl, CustomWebViewClient(scripts, requestIncludeFilter, requestExcludeFilter))