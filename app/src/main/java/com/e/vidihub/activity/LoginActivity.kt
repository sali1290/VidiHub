package com.e.vidihub.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivityLoginBinding
import com.e.vidihub.viewmodel.LoginViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (this as AppCompatActivity).supportActionBar!!.hide()

        //give progressBar custom view
        val wave: Sprite = Wave()
        wave.color = this.getColor(R.color.primary_color)
        binding.loginProgressBar.indeterminateDrawable = wave

        setItemsOnClicks()
    }


    private fun setItemsOnClicks() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://185.171.53.51:9092/register")
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://185.171.53.51:9092/register")
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            val pass = binding.tvPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                    this,
                    "لطفا تمامی مقادیر را وارد کنید",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.login(email, pass)
                observe()
            }
        }
    }

    private fun observe() {
        sessionManager = SessionManager(this)
        viewModel.loginResponse.observe(this) {
            when (it) {

                is Result.Success -> {
                    binding.loginProgressBar.visibility = View.GONE
                    sessionManager.saveAuthToken(it.data.accessToken)
                    sessionManager.saveRefreshToken(it.data.refreshToken)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.loginProgressBar.visibility = View.GONE

                    //for showing error if username or password is not valid
                    when {
                        it.message.contains("Invalid password") -> {
                            binding.tvPassword.error = "رمز عبور اشتباه است"
                        }
                        it.message.contains("Invalid username") -> {
                            binding.tvEmail.error = "نام کاربری اشتباه است"
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "نام کاربری یا رمز عبور صحیح نیست",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            }
        }


    }


}