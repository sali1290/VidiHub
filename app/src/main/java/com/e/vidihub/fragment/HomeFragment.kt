package com.e.vidihub.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.adapter.HomeViewPagerAdapter
import com.e.vidihub.databinding.FragmentHomeBinding
import com.e.vidihub.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawer: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.videosPager.adapter = HomeViewPagerAdapter(requireContext())

        //fro scroll viewpager2
//        val countDownTimer = object : CountDownTimer(60000, 6000) {
//            override fun onTick(millisUntilFinished: Long) {
//                if (binding.videosPager.currentItem == 2) {
//                    binding.videosPager.currentItem = 0
//                } else {
//                    binding.videosPager.currentItem++
//                }
//            }
//
//            override fun onFinish() {
//                start()
//            }
//        }.start()


        val slidingImageDots: MutableList<ImageView> = ArrayList()
        for (i in 0 until 3) {
            slidingImageDots.add(ImageView(requireContext()))
            slidingImageDots[i].setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.non_active_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.sliderDots.addView(slidingImageDots[i], params)
        }
        slidingImageDots[0].setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot
            )
        )
        val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until 3) {
                    slidingImageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.non_active_dot
                        )
                    )
                }

                slidingImageDots[position].setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.active_dot
                    )
                )
            }
        }
        binding.videosPager.registerOnPageChangeCallback(slidingCallback)


        setUpDrawerItems()
        setUpBottomNav()

        userViewModel.getUser()
        observeDomain()

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

        drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain).text = "test"

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_support -> {
                    Toast.makeText(requireContext(), "support", Toast.LENGTH_LONG).show()
                }

                R.id.item_about_us -> {
                    Toast.makeText(requireContext(), "about us", Toast.LENGTH_LONG).show()
                }

                R.id.item_gallery -> {
                    Toast.makeText(requireContext(), "gallery", Toast.LENGTH_LONG).show()
                }

                R.id.item_exit -> {

                    AlertDialog.Builder(
                        requireContext()
                    ).setTitle("خروج از حساب کاربری؟")
                        .setPositiveButton("بله") { _, _ ->
                            val sessionManager = SessionManager(requireContext())
                            sessionManager.saveAuthToken("")
                            requireActivity().finish()
                        }.setNegativeButton("خیر") { _, _ -> }.show()
                }

            }

            return@setNavigationItemSelectedListener true
        }
    }

    private fun observeDomain() {
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.progressBar)
        userViewModel.user.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    drawer = requireActivity().findViewById(R.id.nav_view)
                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain).text =
                        "domain: ${it.data.domain}"
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

