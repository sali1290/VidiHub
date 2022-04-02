package com.e.vidihub.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class NativeVideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNativeVideoCallBinding

    private var username = ""

    private var supportUser = "vidihub"

    //    private var supportUser = ""
    private var isPeerConnected = false

//    var firebaseRef = Firebase.database.getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username")!!
        username = username.split(".")[0]

        askPermissions()

        binding.btnCall.setOnClickListener {
//            supportUser = binding.etPersonName.text.toString()
            sendCallRequest()
        }

        setUpWebView()
    }

    private fun sendCallRequest() {
        if (!isPeerConnected) {
            Toast.makeText(this, "You are not connected", Toast.LENGTH_SHORT).show()
            return
        }
        listenForConnId()
//        firebaseRef.child(supportUser).child("incoming").setValue(username)
//        firebaseRef.child(supportUser).child("isAvailable")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.value.toString() == "true") {
//                        listenForConnId()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
    }

    private fun listenForConnId() {

//        switchToControls()
        callJsFunction("javascript:startCall(\"${supportUser}\")")
//        firebaseRef.child(supportUser).child("connId")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.value == null)
//                        return
//
//                    switchToControls()
//                    callJsFunction("javascript:startcall(\"${snapshot.value}\")")
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
    }

    private fun setUpWebView() {
        binding.webrtcWebView.apply {
            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            addJavascriptInterface(JavascriptInterface(this@NativeVideoCallActivity), "Android")
        }

        loadVideoCall()
    }

    private fun loadVideoCall() {

        val filePath = "file:android_asset/call.html"
        binding.webrtcWebView.loadUrl(filePath)
        binding.webrtcWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }

        }
    }

    private var uniqueId = ""

    private fun initializePeer() {

//        uniqueId = getUniqueID()
//        Log.i("test reach tag", "reached")
        callJsFunction("javascript:init(\"${username}\", \"$username\")")
//        onCallRequest(username)
//        firebaseRef.child(username).child("incoming")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    onCallRequest(snapshot.value as? String)
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
    }

//    private fun onCallRequest(caller: String?) {
//        if (caller == null) return
//
//        binding.callLayout.visibility = View.VISIBLE
//        binding.incomingCallText.text = "$caller is calling..."
//
//        binding.btnAccept.setOnClickListener {
////            firebaseRef.child(username).child("connId").setValue(uniqueId)
////            firebaseRef.child(username).child("isAvailable").setValue(true)
//
//            binding.callLayout.visibility = View.GONE
//
//            switchToControls()
//        }
//
//        binding.btnReject.setOnClickListener {
////            firebaseRef.child(username).child("isAvailable").setValue(null)
//        }
//    }

    private fun switchToControls() {
        binding.inputLayout.visibility = View.GONE

    }

//    private fun getUniqueID(): String {
//        return UUID.randomUUID().toString()
//    }

    private fun callJsFunction(functionString: String) {
        binding.webrtcWebView.post {
            binding.webrtcWebView.evaluateJavascript(
                functionString,
                null
            )
        }
    }


    //ask permissions
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
//        firebaseRef.child(username).setValue(null)
        binding.webrtcWebView.loadUrl("about:blank")
        super.onDestroy()
    }

    fun onPeerConnected() {
        isPeerConnected = true
    }

}

