package com.e.vidihub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.adapter.VideoListAdapter
import com.e.vidihub.databinding.FragmentVideosListBinding
import com.e.vidihub.viewmodel.GetAllVideosViewModel
import com.google.android.material.tabs.TabLayout

class VideosListFragment : Fragment() {

    private lateinit var binding: FragmentVideosListBinding
    private lateinit var viewModel: GetAllVideosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentVideosListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[GetAllVideosViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.videosRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.getAllVideos()
        observe()

        binding.categoryTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {

                    0 -> {
                        viewModel.getAllVideos()
                        observe()
                    }
//
//                    1 -> {
//                        binding.videosRecycler.adapter = VideoListAdapter(6, requireContext())
//                    }
//
//                    2 -> {
//                        binding.videosRecycler.adapter = VideoListAdapter(8, requireContext())
//                    }
//
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }

    private fun observe() {
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.progressBar)
        viewModel.videos.observe(viewLifecycleOwner, {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    binding.videosRecycler.adapter =
                        VideoListAdapter(it.data, requireContext(), requireActivity())
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                }

            }

        })

    }


}