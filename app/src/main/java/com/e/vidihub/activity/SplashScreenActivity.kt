package com.e.vidihub.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivitySplashScreenBinding
import com.e.vidihub.viewmodel.RefreshTokenViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.Wave
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivitySplashScreenBinding
    private val refreshTokenViewModel: RefreshTokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (this as AppCompatActivity?)!!.supportActionBar!!.hide()

        //animate splash screen
        val leftSlideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide_left)
        binding.imgSplash.startAnimation(leftSlideAnimation)
        val rightSlideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide_right)
        binding.tvSplashTitle.startAnimation(rightSlideAnimation)

        Handler().postDelayed(object : Runnable {
            override fun run() {
                checkToken()
            }
        }, 1000)

    }

    override fun onBackPressed() {}

    //check if token is stile usable or not
    private fun checkToken() {
        sessionManager = SessionManager(this)
        if (!sessionManager.fetchAuthToken().isNullOrEmpty()) {
            refreshTokenViewModel.refreshToken(sessionManager.fetchRefreshToken()!!)
            observeRefreshToken()
        } else {
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }
    }

    //request for new token if current token is expired
    private fun observeRefreshToken() {
        refreshTokenViewModel.token.observe(this) {
            when (it) {

                is Result.Success -> {
                    binding.waitLayout.visibility = View.GONE
                    sessionManager.saveAuthToken(it.data)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {
                    showProgressWhileLoad()
                }

                is Result.Error -> {
                    binding.waitLayout.visibility = View.GONE
                    if (it.message.contains("401")) {
                        sessionManager.saveAuthToken("")
                        sessionManager.saveRefreshToken("")
                    }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            }
        }
    }

    private fun showProgressWhileLoad() {
        val doubleBounce: Sprite = DoubleBounce()
        doubleBounce.color = this.getColor(R.color.primary_color)
        binding.waitProgressBar.indeterminateDrawable = doubleBounce

        binding.waitLayout.visibility = View.VISIBLE
    }
}