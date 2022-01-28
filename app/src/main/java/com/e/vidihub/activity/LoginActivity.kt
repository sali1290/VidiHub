package com.e.vidihub.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivityLoginBinding
import com.e.vidihub.viewmodel.LoginViewModel
import com.e.vidihub.viewmodel.RefreshTokenViewModel
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
        setItemsOnClicks()
    }


    private fun setItemsOnClicks() {
        binding.tvRegister.setOnClickListener {
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
        val progressBar: ProgressBar = this.findViewById(R.id.login_progressBar)
        sessionManager = SessionManager(this)
        viewModel.loginResponse.observe(this, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    sessionManager.saveAuthToken(it.data.accessToken)
                    sessionManager.saveRefreshToken(it.data.refreshToken)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE

                    if (it.message.contains("401")) {
                        Toast.makeText(
                            this,
                            "نام کاربری یا رمز عبور نادرست است",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }


}