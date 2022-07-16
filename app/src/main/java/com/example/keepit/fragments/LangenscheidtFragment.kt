package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewClient

private const val defaultUrl: String = "https://de.langenscheidt.com/deutsch-arabisch/gehen"
//private const val defaultUrl: String = "https://de.langenscheidt.com/deutsch-englisch/bow"

class LangenscheidtFragment : CustomWebViewFragment(defaultUrl, CustomWebViewClient())