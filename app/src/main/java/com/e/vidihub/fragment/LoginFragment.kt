package com.e.vidihub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.e.domain.utile.Result
import com.e.vidihub.databinding.FragmentLoginBinding
import com.e.vidihub.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater , container , false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.login("info@vidihub.ir", "12345")
//        observe()
    }

    private fun observe() {
        viewModel.loginResponse.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    Toast.makeText(requireContext(), " Success", Toast.LENGTH_LONG).show()
                }

                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }

                is Result.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

            }
        })
    }

}