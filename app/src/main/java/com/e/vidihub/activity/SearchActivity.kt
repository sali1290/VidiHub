package com.e.vidihub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.ActivitySearchBinding
import com.e.vidihub.viewmodel.GetCategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: GetCategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCategories()
    }

    private fun observe() {
        viewModel.category.observe(this, {
            when (it) {

                is Result.Success -> {
                    val list = mutableListOf<String>()
                    for (i in 0 until it.data.size) {
                        list.add(it.data[i].name!!)
                    }

                    binding.spinnerCategory.adapter = ArrayAdapter<String>(
                        this, android.R.layout.simple_dropdown_item_1line,
                        list
                    )
                }

                is Result.Loading -> {

                }

                is Result.Error -> {

                }

            }
        })


    }


}