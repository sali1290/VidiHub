package com.e.vidihub.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.e.vidihub.R
import com.e.vidihub.adapter.HomeViewPagerAdapter
import com.e.vidihub.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawer: NavigationView
    lateinit var bottomNav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.videosPager.adapter = HomeViewPagerAdapter(requireContext())
        val countDownTimer = object : CountDownTimer(60000, 6000) {
            override fun onTick(millisUntilFinished: Long) {
                if (binding.videosPager.currentItem == 2) {
                    binding.videosPager.currentItem = 0
                } else {
                    binding.videosPager.currentItem++
                }
            }

            override fun onFinish() {
                start()
            }
        }.start()

        setUpDrawerItems()
        setUpBottomNav()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

    }

    private fun setUpBottomNav() {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.btm_home

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.btm_home -> {
                    return@setOnItemSelectedListener true
                }

                R.id.btm_profile -> {
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                    findNavController().navigate(R.id.profileFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.btm_videos -> {
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                    findNavController().navigate(R.id.videoFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }


        }


    }

    private fun setUpDrawerItems() {
        drawer = requireActivity().findViewById(R.id.nav_view)

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_videos -> {
                    Toast.makeText(requireContext(), "videos", Toast.LENGTH_LONG).show()
                }

                R.id.item_profile -> {
                    Toast.makeText(requireContext(), "profile", Toast.LENGTH_LONG).show()
                }

                R.id.item_about_us -> {
                    Toast.makeText(requireContext(), "about us", Toast.LENGTH_LONG).show()
                }

                R.id.item_gallery -> {
                    Toast.makeText(requireContext(), "gallery", Toast.LENGTH_LONG).show()
                }

            }

            return@setNavigationItemSelectedListener true
        }
    }


}