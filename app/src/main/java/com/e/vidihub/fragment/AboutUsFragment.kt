package com.e.vidihub.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.e.vidihub.BuildConfig
import com.e.vidihub.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.tvAppVersion.text = "Version: ${BuildConfig.VERSION_NAME}"

        binding.tvSuggest.setOnClickListener {
            val emailAddress = "Vidihub.test@gmail.com"
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$emailAddress")
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }


        binding.tvIntroduce.setOnClickListener {
            ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setChooserTitle("Share this text with: ") //title for the app chooser
                .setText("download vidihub") // intent data
                .startChooser() // send the intent
        }


        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}