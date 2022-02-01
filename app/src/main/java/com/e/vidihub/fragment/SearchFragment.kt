package com.e.vidihub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.e.domain.model.CategoryResponseModel
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.adapter.LoaderStateAdapter
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.adapter.PagingVideoWithNameAdapter
import com.e.vidihub.databinding.FragmentSearchBinding
import com.e.vidihub.viewmodel.CategoriesViewModel
import com.e.vidihub.viewmodel.GetAllVideosViewModel
import com.e.vidihub.viewmodel.VideoPagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    private val categoriesId = mutableListOf<CategoryResponseModel>()
    private val videosName = mutableListOf<String>()

    private val categorySearch: VideoPagingViewModel by viewModels()
    private val viewModel: CategoriesViewModel by viewModels()
    private val videosViewModel: GetAllVideosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        binding!!.searchedVideoRecycler.layoutManager = GridLayoutManager(requireActivity(), 2)
        viewModel.getCategories()
        videosViewModel.getAllVideos()
        observesAllVideosName()
        observe()

        binding!!.btnSearch.setOnClickListener {
            var id = ""
            val name = binding!!.tvVideoName.text.toString()
            if (videosName.contains(name)) {
                for (i in 0 until categoriesId.size) {
                    if (binding!!.spinnerCategory.selectedItem.toString() == categoriesId[i].name) {
                        id = categoriesId[i].cid!!
                    }
                }
                observeVideosWithCategoryAndName(id, name)
            } else {
                for (i in 0 until categoriesId.size) {
                    if (binding!!.spinnerCategory.selectedItem.toString() == categoriesId[i].name) {
                        id = categoriesId[i].cid!!
                    }
                }
                observeVideosWithCategory(id)
            }

        }
    }

    private fun observe() {
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.search_progressBar)
        viewModel.category.observe(requireActivity(), {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val list = mutableListOf<String>()
                    for (i in 0 until it.data.size) {
                        list.add(it.data[i].name!!)
                    }
                    for (i in 0 until it.data.size) {
                        categoriesId.add(it.data[i])
                    }

                    binding!!.spinnerCategory.adapter = ArrayAdapter(
                        requireActivity(), android.R.layout.simple_dropdown_item_1line,
                        list
                    )
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })


    }

    private fun observeVideosWithCategory(category: String) {
        try {
            lifecycleScope.launch {
                categorySearch.fetchSearchedVideosLiveData(category).observe(requireActivity(), {
                    val adapter = PagingVideoAdapter(requireActivity(), requireActivity())
                    adapter.submitData(lifecycle, it)
                    val loaderStateAdapter = LoaderStateAdapter {
                        adapter.retry()
                    }
                    binding!!.searchedVideoRecycler.adapter =
                        adapter.withLoadStateFooter(loaderStateAdapter)
                    adapter.updateData()
                })
            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeVideosWithCategoryAndName(category: String, name: String) {
        try {
            lifecycleScope.launch {
                categorySearch.fetchSearchedVideosLiveData(category).observe(requireActivity(), {
                    val adapter =
                        PagingVideoWithNameAdapter(requireActivity(), requireContext(), name)
                    adapter.submitData(lifecycle, it)
                    val loaderStateAdapter = LoaderStateAdapter {
                        adapter.retry()
                    }
                    binding!!.searchedVideoRecycler.adapter =
                        adapter.withLoadStateFooter(loaderStateAdapter)
                    adapter.updateData()
                })
            }
        } catch (e: NullPointerException) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observesAllVideosName() {
        val progressBar: ProgressBar = requireActivity().findViewById(R.id.search_progressBar)
        videosViewModel.videos.observe(requireActivity(), {
            when (it) {

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    for (i in 0 until it.data.size) {
                        videosName.add(it.data[i].title!!)
                    }
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}