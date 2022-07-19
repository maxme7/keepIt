package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewClient
import com.example.keepit.webview.CustomWebViewFragment

private const val defaultUrl: String = "https://de.langenscheidt.com/deutsch-arabisch/gehen"
private val requestIncludeFilter = arrayOf("langenscheidt", "cloudfront", "svg")
private val scripts = arrayOf("playAudioDirectly.js", "addStars.js")


class LangenscheidtFragment : CustomWebViewFragment(defaultUrl, CustomWebViewClient(scripts, requestIncludeFilter))