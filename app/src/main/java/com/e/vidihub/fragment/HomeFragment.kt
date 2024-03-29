package com.e.vidihub.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.e.data.util.SessionManager
import com.e.domain.model.VideoPosterModel
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.activity.PlayVideoActivity
import com.e.vidihub.activity.SplashScreenActivity
import com.e.vidihub.activity.VideoCallActivity
import com.e.vidihub.adapter.HomeViewPagerAdapter
import com.e.vidihub.adapter.HomeViewPagerAdapter.OnPlayClickListener
import com.e.vidihub.databinding.FragmentHomeBinding
import com.e.vidihub.viewmodel.DomainViewModel
import com.e.vidihub.viewmodel.UserViewModel
import com.e.vidihub.viewmodel.VideoPosterViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), OnPlayClickListener {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawer: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var sessionManager: SessionManager
    private lateinit var progressBar: ProgressBar
    private lateinit var countDownTimer: CountDownTimer
    private var videoPosterList = mutableListOf<VideoPosterModel>()

    private val domainViewModel: DomainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val videoPosterViewModel: VideoPosterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //for videoPosters
        videoPosterViewModel.getVideoPosters()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.apply {
            show()
            //if you want to implement dark mode you should delete below code
            setBackgroundDrawable(ColorDrawable(requireActivity().getColor(R.color.primary_color)))
        }

        progressBar = requireActivity().findViewById(R.id.progressBar)
        createCustomProgressBar(progressBar)


        drawer = binding.navView
        setUpDrawerItems()
        setUpBottomNav()

        //for user account
        userViewModel.getUser()
        observeUserEmail()

        //for domain
        domainViewModel.getDomain()
        observeDomain()

        observeVideosPosters()

        //for auto scroll viewpager2
        countDownTimer = object : CountDownTimer(6000, 6000) {
            override fun onTick(millisUntilFinished: Long) {
                if (binding.videosPager.currentItem == videoPosterList.size - 1) {
                    binding.videosPager.currentItem = 0
                } else {
                    binding.videosPager.currentItem++
                }
            }

            override fun onFinish() {
                start()
            }
        }.start()

        //check if navigation drawer is open close it otherwise close app
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.navLayout.isOpen) {
                        binding.navLayout.close()
                    } else {
                        requireActivity().finish()
                    }
                }
            })

    }

    private fun createCustomProgressBar(progressBar: ProgressBar) {
        val wave: Sprite = Wave()
        wave.color = requireContext().getColor(R.color.primary_color)
        progressBar.indeterminateDrawable = wave
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
        drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_support -> {
                    //   Toast.makeText(requireContext(), "support", Toast.LENGTH_LONG).show()
                    requireActivity().startActivity(
                        Intent(
                            requireContext(),
                            VideoCallActivity::class.java
                        )
                    )
                }

                R.id.item_about_us -> {
                    findNavController().navigate(R.id.aboutUsFragment)
                }

                R.id.item_exit -> {

                    AlertDialog.Builder(
                        requireContext()
                    ).setTitle("خروج از حساب کاربری؟")
                        .setPositiveButton("بله") { _, _ ->
                            sessionManager.saveAuthToken("")
                            sessionManager.saveRefreshToken("")
                            startActivity(
                                Intent(
                                    requireContext(),
                                    SplashScreenActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }.setNegativeButton("خیر") { _, _ -> }.show()
                }

            }

            return@setNavigationItemSelectedListener true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeDomain() {
        domainViewModel.domainInfo.observe(viewLifecycleOwner) {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain).text =
                        "Domain: ${it.data.hostname}"

                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_domain_status).text =
                        "Status : ${it.data.status}"

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
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUserEmail() {
        userViewModel.user.observe(viewLifecycleOwner) {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    drawer.getHeaderView(0).findViewById<TextView>(R.id.tv_email).text =
                        "Account : ${it.data.email}"
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun observeVideosPosters() {
        videoPosterViewModel.videos.observe(requireActivity()) {
            when (it) {

                is Result.Success -> {
                    videoPosterList.clear()
                    binding.videosPager.adapter =
                        HomeViewPagerAdapter(it.data, requireContext(), this)

                    for (i in 0 until it.data.size)
                        videoPosterList.add(it.data[i])

                    setUpBottomDots(videoPosterList.size)
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun setUpBottomDots(size: Int) {
        //dots below the screen
        val slidingImageDots: MutableList<ImageView> = ArrayList()
        for (i in 0 until size) {
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
                for (i in 0 until size) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    override fun onPlayClick(position: Int) {
        val intent = Intent(requireContext(), PlayVideoActivity::class.java)
        intent.putExtra("video link", videoPosterList[position].videoId)
        startActivity(intent)
    }
}

