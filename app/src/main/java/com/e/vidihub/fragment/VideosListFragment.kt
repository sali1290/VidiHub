package com.e.vidihub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.e.vidihub.adapter.VideoListAdapter
import com.e.vidihub.databinding.FragmentVideosListBinding
import com.google.android.material.tabs.TabLayout

class VideosListFragment : Fragment() {

    private lateinit var binding: FragmentVideosListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentVideosListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.videosRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.videosRecycler.adapter = VideoListAdapter(10, requireContext())

        binding.categoryTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {

                    0 -> {
                        binding.videosRecycler.adapter = VideoListAdapter(10, requireContext())
                    }

                    1 -> {
                        binding.videosRecycler.adapter = VideoListAdapter(6, requireContext())
                    }

                    2 -> {
                        binding.videosRecycler.adapter = VideoListAdapter(8, requireContext())
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }


}