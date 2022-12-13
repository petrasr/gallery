package com.example.gallery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.R
import com.example.gallery.data.model.GalleryItem
import com.example.gallery.databinding.ItemGalleryBinding

class GalleryAdapter(
    private val onItemClick: (imageUrl: String) -> Unit
) : ListAdapter<GalleryItem, GalleryAdapter.ItemViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view) { adapterPosition ->
            currentList[adapterPosition].media?.imageUrl?.let { url ->
                onItemClick(url)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<GalleryItem>() {
        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.media?.imageUrl == newItem.media?.imageUrl && oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
             oldItem.media?.imageUrl == newItem.media?.imageUrl && oldItem.title == newItem.title
    }

    class ItemViewHolder constructor(
        private val binding: ItemGalleryBinding,
        private val onItemClick: (adapterPosition: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(item: GalleryItem) {
            Glide
                .with(binding.imageView.context)
                .load(item.media?.imageUrl)
                .error(R.drawable.ic_baseline_error_24)
                .into(binding.imageView)
            binding.titleView.text = item.title
        }
    }
}