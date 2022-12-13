package com.example.gallery.data.api

import com.example.gallery.data.model.Gallery
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("services/feeds/photos_public.gne")
    suspend fun getGallery(@Query("format") format: String, @Query("nojsoncallback") noJson: Int): Gallery
}