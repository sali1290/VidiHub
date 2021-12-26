package com.e.vidihub.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.e.vidihub.R
import com.e.vidihub.adapter.HomeViewPagerAdapter
import com.e.vidihub.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var drawer: NavigationView

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
        val countDownTimer = object : CountDownTimer(60000, 4000) {
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
    }

    private fun setUpBottomNav() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
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