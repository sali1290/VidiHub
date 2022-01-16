package com.e.vidihub.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.FragmentLoginBinding
import com.e.vidihub.viewmodel.LoginViewModel

class LoginFragment
    : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        sessionManager = SessionManager(requireContext())
        if (!sessionManager.fetchAuthToken().isNullOrEmpty()) {
            findNavController().navigate(R.id.homeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    requireContext(),
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
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.progressBar)
        viewModel.loginResponse.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    sessionManager.saveAuthToken(it.data.accessToken)
                    sessionManager.saveRefreshToken(it.data.refreshToken)
                    findNavController().navigate(R.id.homeFragment)
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE

                    if (it.message.contains("401")) {
                        Toast.makeText(
                            requireContext(),
                            "نام کاربری یا رمز عبور نادرست است",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }

}