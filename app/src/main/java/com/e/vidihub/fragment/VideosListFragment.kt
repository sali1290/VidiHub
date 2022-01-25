package com.e.vidihub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.e.vidihub.adapter.LoaderStateAdapter
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.databinding.FragmentVideosListBinding
import com.e.vidihub.viewmodel.GetVideoPagingListViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosListFragment : Fragment() {

    private lateinit var binding: FragmentVideosListBinding

    private val listViewModel: GetVideoPagingListViewModel by viewModels()

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
                    val adapter = PagingVideoAdapter(requireActivity(), requireContext())
                    adapter.submitData(lifecycle, it)
                    val loaderStateAdapter = LoaderStateAdapter {
                        adapter.retry()
                    }
                    binding.videosRecycler.adapter = adapter.withLoadStateFooter(loaderStateAdapter)

                })

            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}