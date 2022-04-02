package com.e.vidihub.jsinterface

import android.webkit.JavascriptInterface
import com.e.vidihub.activity.NativeVideoCallActivity

class JavascriptInterface(private val nativeVideoCallActivity: NativeVideoCallActivity) {

    @JavascriptInterface
    public fun onPeerConnected() {
        nativeVideoCallActivity.onPeerConnected()
    }

}