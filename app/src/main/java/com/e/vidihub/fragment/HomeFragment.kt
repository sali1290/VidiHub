package com.e.vidihub.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.e.data.util.SessionManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.activity.LoginActivity
import com.e.vidihub.adapter.HomeViewPagerAdapter
import com.e.vidihub.databinding.FragmentHomeBinding
import com.e.vidihub.viewmodel.GetDomainViewModel
import com.e.vidihub.viewmodel.RefreshTokenViewModel
import com.e.vidihub.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawer: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var sessionManager: SessionManager
    private lateinit var progressBar: ProgressBar
    private lateinit var countDownTimer: CountDownTimer

    private val domainViewModel: GetDomainViewModel by viewModels()
    private val refreshTokenViewModel: RefreshTokenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.videosPager.adapter = HomeViewPagerAdapter(requireContext())
        progressBar = requireActivity().findViewById(R.id.progressBar)

        //fro scroll viewpager2
        countDownTimer = object : CountDownTimer(6000, 6000) {
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

        domainViewModel.getDomain()
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

        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_support -> {
                    Toast.makeText(requireContext(), "support", Toast.LENGTH_LONG).show()
                }

                R.id.item_about_us -> {
                    Toast.makeText(requireContext(), "about us", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.aboutUsFragment)
                }

                R.id.item_gallery -> {
                    Toast.makeText(requireContext(), "gallery", Toast.LENGTH_LONG).show()
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "video/*"
                    startActivity(intent)
                }

                R.id.item_exit -> {

                    AlertDialog.Builder(
                        requireContext()
                    ).setTitle("خروج از حساب کاربری؟")
                        .setPositiveButton("بله") { _, _ ->
                            sessionManager.saveAuthToken("")
                            sessionManager.saveRefreshToken("")
                            requireActivity().finish()
                        }.setNegativeButton("خیر") { _, _ -> }.show()
                }

            }

            return@setNavigationItemSelectedListener true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeDomain() {
        domainViewModel.domainInfo.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    drawer = requireActivity().findViewById(R.id.nav_view)
                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain).text =
                        "domain: ${it.data.hostname}"

                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain_status).text =
                        "status : ${it.data.status}"
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE

                    if (it.message == "لطفا اتصال اینترنت خود را بررسی کنید") {
                        AlertDialog.Builder(requireContext()).setCancelable(false)
                            .setMessage("اتصال اینترنت برقرار نیست! لظفا مجددا تلاش کنید")
                            .setNegativeButton(
                                "خروج"
                            ) { dialog, which ->
                                requireActivity().finish()
                            }.setPositiveButton("تلاش مجدد") { dialog, which ->
                                requireActivity().recreate()
                            }.show()
                    } else if (it.message.contains("401")) {
                        refreshTokenViewModel.refreshToken(sessionManager.fetchRefreshToken()!!)
                        observeRefreshToken()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                }

            }
        })
    }

    private fun observeRefreshToken() {
        refreshTokenViewModel.token.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    sessionManager.saveAuthToken(it.data)
                    requireActivity().recreate()
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    sessionManager.saveAuthToken("")
                    sessionManager.saveRefreshToken("")
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}

