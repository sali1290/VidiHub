package com.e.vidihub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.databinding.FragmentVideosListBinding
import com.e.vidihub.viewmodel.GetVideoPagingListViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class VideosListFragment : Fragment() {

    private lateinit var binding: FragmentVideosListBinding
    private lateinit var listViewModel: GetVideoPagingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentVideosListBinding.inflate(inflater, container, false)
        listViewModel =
            ViewModelProvider(requireActivity())[GetVideoPagingListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.videosRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        observe()

        binding.categoryTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {

                    0 -> {
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
        try {
            lifecycleScope.launch {
                listViewModel.fetchVideosLiveData().observe(viewLifecycleOwner, {
                    val adapter = PagingVideoAdapter(requireContext(), requireActivity())
                    adapter.submitData(lifecycle, it)
                    binding.videosRecycler.adapter = adapter

                })

            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}