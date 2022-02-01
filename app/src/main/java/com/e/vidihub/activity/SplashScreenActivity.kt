package com.e.vidihub.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.viewmodel.RefreshTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val refreshTokenViewModel: RefreshTokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        (this as AppCompatActivity?)!!.supportActionBar!!.hide()

        Handler().postDelayed(object : Runnable {
            override fun run() {
                checkToken()
            }
        }, 2500)

    }

    override fun onBackPressed() {}

    private fun checkToken() {
        sessionManager = SessionManager(this)
        if (!sessionManager.fetchAuthToken().isNullOrEmpty()) {
            refreshTokenViewModel.refreshToken(sessionManager.fetchRefreshToken()!!)
            observeRefreshToken()
        } else {
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        }
    }

    private fun observeRefreshToken() {
        refreshTokenViewModel.token.observe(this, {
            when (it) {

                is Result.Success -> {
                    sessionManager.saveAuthToken(it.data)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    if (it.message.contains("401")) {
                        sessionManager.saveAuthToken("")
                        sessionManager.saveRefreshToken("")
                    }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            }
        })
    }

}