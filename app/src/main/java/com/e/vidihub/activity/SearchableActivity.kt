package com.e.vidihub.activity

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.e.vidihub.R
import com.e.vidihub.adapter.LoaderStateAdapter
import com.e.vidihub.adapter.PagingVideoAdapter
import com.e.vidihub.databinding.ActivitySearchableBinding
import com.e.vidihub.provider.VideoSuggestionProvider
import com.e.vidihub.viewmodel.VideoPagingViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchableActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchableBinding
    private val getSearchedVideosWithNameViewModel: VideoPagingViewModel by viewModels()

    //alternative way for get searched videos
    //  private val getAllVideosViewModel: GetAllVideosViewModel by viewModels()
    //  private var searchedVideos = mutableListOf<VideoListItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as AppCompatActivity).supportActionBar!!.hide()
        binding = ActivitySearchableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wave: Sprite = Wave()
        wave.color = this.getColor(R.color.primary_color)
        binding.searchResultProgressBar.indeterminateDrawable = wave

        //get search query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->

                //save queries into content provider
                SearchRecentSuggestions(
                    this,
                    VideoSuggestionProvider.AUTHORITY,
                    VideoSuggestionProvider.MODE
                ).saveRecentQuery(query, null)


                //initialize search result recycler
                binding.searchResultRecycler.layoutManager = GridLayoutManager(this, 2)
                //alternative way for get searched videos
//              getAllVideosViewModel.getAllVideos()
                observeAllNames(query)
            }
        }

        binding.imgDeleteHistory.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("پاک کردن تاریخچه جست و جو")
                .setMessage("تمام جست و جو های گذشته شما پاک خواهد شد. آیا از این کار مطمئن هستید؟")
                .setPositiveButton("بله") { dialogInterface, i ->
                    SearchRecentSuggestions(
                        this, VideoSuggestionProvider.AUTHORITY,
                        VideoSuggestionProvider.MODE
                    ).clearHistory()
                    Snackbar.make(
                        binding.imgDeleteHistory,
                        "تاریخچه جست و جو با موفقیت پاک شد",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("خیر") { dialogInterface, i -> }.show()
        }


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun observeAllNames(query: String) {
        try {
            lifecycleScope.launch {
                getSearchedVideosWithNameViewModel.fetchSearchedVideosWithNameLiveData(query)
                    .observe(this@SearchableActivity) {
                        val adapter =
                            PagingVideoAdapter(this@SearchableActivity, this@SearchableActivity)
                        adapter.submitData(lifecycle, it)
                        val loaderStateAdapter = LoaderStateAdapter {
                            adapter.retry()
                        }
                        binding.searchResultRecycler.adapter =
                            adapter.withLoadStateFooter(loaderStateAdapter)
                    }
            }
        } catch (e: NullPointerException) {
            Toast.makeText(this@SearchableActivity, e.toString(), Toast.LENGTH_SHORT).show()
        }
        //alternative way for get searched videos
//        getAllVideosViewModel.videos.observe(this) {
//            when (it) {
//
//                is Result.Success -> {
//                    binding.searchResultProgressBar.visibility = View.GONE
//
//                    for (i in 0 until it.data.size) {
//                        if (it.data[i].title!!.contains(query)) {
//                            searchedVideos.add(it.data[i])
//                        }
//                    }
//                    binding.searchResultRecycler.adapter =
//                        SearchVideoAdapter(searchedVideos, this, this)
//
//                }
//
//                is Result.Loading -> {
//                    binding.searchResultProgressBar.visibility = View.VISIBLE
//                }
//
//                is Result.Error -> {
//                    binding.searchResultProgressBar.visibility = View.GONE
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }


    }


}