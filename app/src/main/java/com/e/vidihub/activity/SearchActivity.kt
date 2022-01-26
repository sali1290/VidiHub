package com.e.vidihub.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.e.domain.model.CategoryResponseModel
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.adapter.LoaderStateAdapter
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.databinding.ActivitySearchBinding
import com.e.vidihub.viewmodel.GetCategoriesViewModel
import com.e.vidihub.viewmodel.VideoPagingListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val categoriesId = mutableListOf<CategoryResponseModel>()

    private val categorySearch: VideoPagingListViewModel by viewModels()
    private val viewModel: GetCategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this as AppCompatActivity).supportActionBar!!.hide()

        binding.searchedVideoRecycler.layoutManager = GridLayoutManager(this, 2)
        viewModel.getCategories()
        observe()

        binding.btnSearch.setOnClickListener {
            var id = ""
            for (i in 0 until categoriesId.size) {
                if (binding.spinnerCategory.selectedItem.toString() == categoriesId[i].name) {
                    id = categoriesId[i].cid!!
                }
            }
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
            observeVideosWithCategory(id)
        }


    }

    private fun observe() {
        val progressBar: ProgressBar = findViewById(R.id.search_progressBar)
        viewModel.category.observe(this, {
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

                    binding.spinnerCategory.adapter = ArrayAdapter(
                        this, android.R.layout.simple_dropdown_item_1line,
                        list
                    )
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })


    }

    private fun observeVideosWithCategory(category: String) {
        try {
            lifecycleScope.launch {
                categorySearch.fetchSearchedVideosLiveData(category).observe(this@SearchActivity, {
                    val adapter = PagingVideoAdapter(this@SearchActivity, this@SearchActivity)
                    adapter.submitData(lifecycle, it)
                    val loaderStateAdapter = LoaderStateAdapter {
                        adapter.retry()
                    }
                    binding.searchedVideoRecycler.adapter =
                        adapter.withLoadStateFooter(loaderStateAdapter)
                    adapter.updateData()
                })
            }
        } catch (e: NullPointerException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}