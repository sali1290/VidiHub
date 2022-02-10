package com.e.vidihub.activity

import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.e.vidihub.databinding.ActivityVideoCallBinding


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

        binding.webrtcWebView.loadUrl(url)
        webSetting = binding.webrtcWebView.settings
        webSetting.javaScriptEnabled = true

    }

    override fun onPause() {
        super.onPause()
        webSetting.javaScriptEnabled = false
    }

}