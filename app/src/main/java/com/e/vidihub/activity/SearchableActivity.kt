package com.e.vidihub.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.e.domain.model.VideoListItemModel
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.adapter.SearchVideoAdapter
import com.e.vidihub.databinding.ActivitySearchableBinding
import com.e.vidihub.viewmodel.GetAllVideosViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchableActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchableBinding
    private val getAllVideosViewModel: GetAllVideosViewModel by viewModels()
    private var searchedVideos = mutableListOf<VideoListItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as AppCompatActivity).supportActionBar!!.hide()
        binding = ActivitySearchableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wave: Sprite = Wave()
        wave.color = this.getColor(R.color.primary_color)
        binding.searchResultProgressBar.indeterminateDrawable = wave

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                binding.searchResultRecycler.layoutManager = GridLayoutManager(this, 2)
                getAllVideosViewModel.getAllVideos()
                observeAllNames(query)
            }
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun observeAllNames(query: String) {

        getAllVideosViewModel.videos.observe(this, {
            when (it) {

                is Result.Success -> {
                    binding.searchResultProgressBar.visibility = View.GONE

                    for (i in 0 until it.data.size) {
                        if (it.data[i].title!!.contains(query)) {
                            searchedVideos.add(it.data[i])
                        }
                    }
                    binding.searchResultRecycler.adapter =
                        SearchVideoAdapter(searchedVideos, this, this)
                }

                is Result.Loading -> {
                    binding.searchResultProgressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.searchResultProgressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })


    }


}