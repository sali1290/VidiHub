package com.e.vidihub.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.activity.MainActivity
import com.e.vidihub.adapter.ProfileAdapter
import com.e.vidihub.databinding.FragmentProfileBinding
import com.e.vidihub.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()
        observe()

        binding.logout.setOnClickListener {

            AlertDialog.Builder(
                requireContext()
            ).setTitle("خروج از حساب کاربری؟")
                .setPositiveButton("بله") { _, _ ->
                    sessionManager.saveAuthToken("")
                    requireActivity().finish()
                }.setNegativeButton("خیر") { _, _ ->}.show()
        }

    }

    private fun observe() {
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.progressBar)
        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    binding.userInfoRecycler.adapter = ProfileAdapter(it.data, requireContext())
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

            }
        })
    }

}