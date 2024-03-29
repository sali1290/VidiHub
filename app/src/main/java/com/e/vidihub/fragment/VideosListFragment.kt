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
import com.e.domain.model.CategoryResponseModel
import com.e.domain.util.Result
import com.e.vidihub.adapter.CategoriesAdapter
import com.e.vidihub.adapter.LoaderStateAdapter
import com.e.vidihub.adapter.OnCategoriesListener
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.databinding.FragmentVideosListBinding
import com.e.vidihub.viewmodel.CategoriesViewModel
import com.e.vidihub.viewmodel.VideoPagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosListFragment : Fragment(), OnCategoriesListener {

    private var _binding: FragmentVideosListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoPagingViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var categories = mutableListOf<CategoryResponseModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentVideosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getCategories()
        observeCategories()

        binding.videosRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        observe()

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun observe() {
        try {
            lifecycleScope.launch {
                viewModel.fetchVideosLiveData().observe(viewLifecycleOwner) {
                    val adapter = PagingVideoAdapter(requireActivity(), requireContext())
                    adapter.submitData(lifecycle, it)
                    val loaderStateAdapter = LoaderStateAdapter {
                        adapter.retry()
                    }
                    binding.videosRecycler.adapter = adapter.withLoadStateFooter(loaderStateAdapter)

                }

            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeCategories() {

        categoriesViewModel.category.observe(viewLifecycleOwner) {

            when (it) {

                is Result.Success -> {
                    categories.add(CategoryResponseModel("", "All Videos", ""))
                    for (i in 0 until it.data.size) {
                        categories.add(it.data[i])
                    }
                    binding.categoriesRecycler.adapter =
                        CategoriesAdapter(categories, this)

                }

                is Result.Loading -> {

                }

                is Result.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

    private fun observeCategoriesVideos(category: String) {
        try {
            lifecycleScope.launch {
                viewModel.fetchSearchedVideosWithCategoryLiveData(category)
                    .observe(viewLifecycleOwner) {
                        val adapter = PagingVideoAdapter(requireActivity(), requireContext())
                        adapter.submitData(lifecycle, it)
                        val loaderStateAdapter = LoaderStateAdapter {
                            adapter.retry()
                        }
                        binding.videosRecycler.adapter =
                            adapter.withLoadStateFooter(loaderStateAdapter)
                    }

            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCategoriesClick(position: Int) {
        if (position == 0) {
            observe()
        } else {
            observeCategoriesVideos(categories[position].cid!!)
        }
    }


}