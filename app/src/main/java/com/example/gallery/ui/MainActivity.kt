package com.example.gallery.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gallery.R
import com.example.gallery.data.api.RetrofitBuilder
import com.example.gallery.databinding.ActivityMainBinding
import com.example.gallery.repository.ApiRepository
import com.example.gallery.utils.Status
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainVMF(ApiRepository(RetrofitBuilder.apiService))
    }
    private val adapter = GalleryAdapter(::onItemClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.recyclerView.adapter = adapter
        binding.reloadButton.setOnClickListener { viewModel.loadGallery() }
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView
        if (viewModel.isActiveSearch()) {
            searchMenuItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery, true)
            searchView.clearFocus()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchQuery(it)
                }
                return true
            }
        })
        searchView.setOnCloseListener {
            viewModel.showAllItems()
            return@setOnCloseListener true
        }
        return true
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gallery.collect {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { data ->
                                if (data.isEmpty()) {
                                    showEmpty(getString(R.string.empty_message))
                                } else {
                                    showContent()
                                    adapter.submitList(data)
                                }
                            } ?: run {
                                showError(getString(R.string.error_message))
                            }

                        }
                        Status.ERROR -> showError(it.message)
                        Status.LOADING -> showLoading()
                    }
                }
            }
        }
    }

    private fun onItemClick(imageUrl: String) {
        startActivity(ImageActivity.getIntent(this, imageUrl))
    }

    private fun showLoading() {
        binding.apply {
            progressbar.isVisible = true
            reloadButton.isVisible = false
            message.isVisible = false
            recyclerView.isVisible = false
        }
    }

    private fun showContent() {
        binding.apply {
            progressbar.isVisible = false
            reloadButton.isVisible = false
            message.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun showError(text: String?) {
        binding.apply {
            progressbar.isVisible = false
            reloadButton.isVisible = true
            message.isVisible = true
            message.text = text
            recyclerView.isVisible = false
        }
    }

    private fun showEmpty(text: String?) {
        binding.apply {
            progressbar.isVisible = false
            reloadButton.isVisible = false
            message.isVisible = true
            message.text = text
            recyclerView.isVisible = false
        }
    }
}