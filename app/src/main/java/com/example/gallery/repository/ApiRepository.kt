package com.example.gallery.repository

import com.example.gallery.data.api.ApiService
import com.example.gallery.data.model.GalleryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ApiRepository(private val apiService: ApiService) {

    fun getGalleryItems(): Flow<List<GalleryItem>> = flow {
        emit(apiService.getGallery("json", 1).items)
    }.flowOn(Dispatchers.IO)
}