package com.aarush.newsapp.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.lifecycleScope
import com.aarush.core.abstraction.BaseActivityBinding
import com.aarush.newsapp.R
import com.aarush.newsapp.databinding.ActivityDetailNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@AndroidEntryPoint
class DetailNewsActivity : BaseActivityBinding<ActivityDetailNewsBinding>() {

    private lateinit var urlNews: String
    private lateinit var sourceNews: String

    override val bindingInflater: (LayoutInflater) -> ActivityDetailNewsBinding
        get() = { ActivityDetailNewsBinding.inflate(layoutInflater) }

    override fun setupView() {
        getDataIntent()
        setHeader()
        setWebView()
    }

    private fun getDataIntent() {
        urlNews = intent.getStringExtra("urlNews") ?: ""
        sourceNews = intent.getStringExtra("sourceNews") ?: ""
    }

    private fun setHeader() {
        with(binding) {
            headerDetail.tvHeader.text = sourceNews
            headerDetail.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        binding.webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    if (!this@DetailNewsActivity.isDestroyed) {
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }
        }
        binding.webview.loadUrl(urlNews)
    }

}