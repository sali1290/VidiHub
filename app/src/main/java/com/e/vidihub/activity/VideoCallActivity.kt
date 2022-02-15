package com.e.vidihub.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivityVideoCallBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave


class VideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoCallBinding

    companion object {
        const val url = "Https://vsupport.cxip.co/chatbox.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (this as AppCompatActivity).supportActionBar?.hide()

//        webSetting = binding.webrtcWebView.settings
        binding.webrtcWebView.settings.javaScriptEnabled = true
        binding.webrtcWebView.webViewClient = WebViewClient()
        binding.webrtcWebView.loadUrl(url)

        val wave: Sprite = Wave()
        wave.color = this.getColor(R.color.primary_color)
        binding.videoCallProgressBar.indeterminateDrawable = wave

        val permissionsMic = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        )
        this.requestPermissions(
            permissionsMic, 1000
        )


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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED
                            )
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    binding.webrtcWebView.webChromeClient = object : WebChromeClient() {
                        override fun onPermissionRequest(request: PermissionRequest) {
                            request.grant(request.resources)
                        }
                    }
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    AlertDialog.Builder(this)
                        .setTitle("دسترسی های لازم داده نشده")
                        .setMessage(resources.getString(R.string.permissions_denied))
                        .setPositiveButton("متوجه شدم") { _, _ ->
                            onBackPressed()
                        }.setCancelable(false)
                        .show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}