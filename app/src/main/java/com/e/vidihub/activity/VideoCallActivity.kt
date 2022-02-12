package com.e.vidihub.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivityVideoCallBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave


class VideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoCallBinding
    private lateinit var webSetting: WebSettings

    companion object {
        const val url = "Https://vsupport.cxip.co/chatbox.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this as AppCompatActivity).supportActionBar?.hide()

        webSetting = binding.webrtcWebView.settings
        binding.webrtcWebView.webViewClient = WebViewClient()
        binding.webrtcWebView.loadUrl(url)

        val wave: Sprite = Wave()
        wave.color = this.getColor(R.color.primary_color)
        binding.videoCallProgressBar.indeterminateDrawable = wave

    }

    override fun onStart() {
        super.onStart()
        webSetting.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        webSetting.javaScriptEnabled = false
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view!!.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.videoCallProgressBar.visibility = View.GONE
        }
    }


}