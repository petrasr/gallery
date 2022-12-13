package com.example.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery.data.model.GalleryItem
import com.example.gallery.repository.ApiRepository
import com.example.gallery.utils.Resource
import com.example.gallery.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private val _gallery = MutableStateFlow<Resource<List<GalleryItem>>>(Resource.loading())
    val gallery: StateFlow<Resource<List<GalleryItem>>> = _gallery

    private var allItems: List<GalleryItem>? = null
    var searchQuery: String = ""
        private set

    init {
        loadGallery()
    }

    fun loadGallery() {
        viewModelScope.launch {
            _gallery.value = Resource.loading()
            apiRepository.getGalleryItems()
                .catch { e ->
                    _gallery.value = Resource.error(e.toString())
                }
                .collect {
                    allItems = it
                    _gallery.value = Resource.success(it)
                }
        }
    }

    fun searchQuery(newText: String) {
        searchQuery = newText
        if (_gallery.value.status == Status.SUCCESS) {
            if (newText.isNotBlank()) {
                allItems?.let { items ->
                    val filtered = items
                        .associateBy({ it }, { it.tags?.tags ?: emptyList() })
                        .filterValues { tags -> tags.any { tag -> tag.contains(newText) } }
                        .map { it.key }
                    _gallery.value = Resource.success(filtered)
                }
            } else {
                _gallery.value = Resource.success(allItems)
            }
        }
    }

    fun isActiveSearch(): Boolean = searchQuery.isNotEmpty()

    fun showAllItems() {
        searchQuery("")
    }
}

@Suppress("UNCHECKED_CAST")
class MainVMF(private val apiRepository: ApiRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(apiRepository) as T
}