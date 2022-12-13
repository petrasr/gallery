package com.example.gallery.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gallery.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {

    companion object {
        private const val IMAGE_URL = "image_url"

        fun getIntent(context: Context, imageUrl: String): Intent =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(IMAGE_URL, imageUrl)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.extras!!.getString(IMAGE_URL)
        Glide
            .with(this)
            .load(url)
            .into(binding.root)
    }
}