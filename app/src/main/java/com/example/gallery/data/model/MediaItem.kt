package com.example.gallery.data.model

import com.google.gson.annotations.SerializedName

data class MediaItem(
    @SerializedName("m")
    val imageUrl: String?
)
