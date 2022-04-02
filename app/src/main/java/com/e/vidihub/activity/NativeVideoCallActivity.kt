package com.e.vidihub.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivityNativeVideoCallBinding
import com.e.vidihub.jsinterface.JavascriptInterface

class NativeVideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNativeVideoCallBinding

    private var username = ""
    private var supportUser = "support"
    private var isPeerConnected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this as AppCompatActivity).supportActionBar!!.hide()
        askPermissions()

        //get user host for username
        username = intent.getStringExtra("username")!!
        setUpWebView()
        setUpButtons()

    }

    //for setup reject and call button functionality
    private fun setUpButtons() {
        binding.apply {

            btnCall.setOnClickListener {
                sendCallRequest()
            }

            btnReject.setOnClickListener {
                onBackPressed()
            }

        }
    }


    private fun sendCallRequest() {
        if (!isPeerConnected) {
            Toast.makeText(this, "You are not connected, please try later", Toast.LENGTH_SHORT)
                .show()
            return
        }
        listenForConnId()

    }

    //call startCall function in js
    private fun listenForConnId() {
        callJsFunction("javascript:startCall(\"${supportUser}\")")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webrtcWebView.apply {
            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            addJavascriptInterface(JavascriptInterface(this@NativeVideoCallActivity), "Android")
        }

        loadVideoCallPage()
    }

    //load html page for WebView
    private fun loadVideoCallPage() {
        val filePath = "file:android_asset/call.html"
        binding.webrtcWebView.loadUrl(filePath)
        binding.webrtcWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }
        }
    }

    //initialize peer with calling init function in js for peerjs server
    private fun initializePeer() {
        callJsFunction("javascript:init(\"${username}\")")
    }

    //for evaluate js function with WebView
    private fun callJsFunction(functionString: String) {
        binding.webrtcWebView.post {
            binding.webrtcWebView.evaluateJavascript(
                functionString,
                null
            )
        }
    }

    //ask permissions for making calls
    private fun askPermissions() {
        val permissionsMic = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        )
        this.requestPermissions(
            permissionsMic, 1000
        )
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

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        binding.webrtcWebView.loadUrl("about:blank")
        super.onDestroy()
    }

    fun onPeerConnected() {
        isPeerConnected = true
    }

}

