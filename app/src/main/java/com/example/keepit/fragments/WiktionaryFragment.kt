package com.example.keepit.fragments

import com.example.keepit.webview.CustomWebViewFragment

private const val defaultUrl: String = "https://de.wiktionary.org/wiki"

class WiktionaryFragment : CustomWebViewFragment(defaultUrl)