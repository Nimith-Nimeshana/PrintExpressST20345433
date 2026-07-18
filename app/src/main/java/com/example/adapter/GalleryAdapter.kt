package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.GalleryItem
import com.example.databinding.ItemGalleryBinding

class GalleryAdapter(
    private var galleryItems: List<GalleryItem>
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.binding.txtGalleryTitle.text = item.title

        val context = holder.itemView.context
        var imageResId = context.resources.getIdentifier(item.image, "drawable", context.packageName)

        if (imageResId == 0) {
            val fallbackName = when (item.image) {
                "business_cards_sample" -> "img_business_cards"
                "flyers_sample" -> "img_flyers"
                "poster_sample" -> "img_posters"
                "img_banners_sample" -> "img_banners"
                "img_wedding_card" -> "img_wedding_cards"
                "img_printed_mug" -> "img_mugs"
                else -> {
                    when {
                        item.image.contains("business", ignoreCase = true) || item.image.contains("card", ignoreCase = true) -> "img_business_cards"
                        item.image.contains("flyer", ignoreCase = true) -> "img_flyers"
                        item.image.contains("poster", ignoreCase = true) -> "img_posters"
                        item.image.contains("banner", ignoreCase = true) -> "img_banners"
                        item.image.contains("wedding", ignoreCase = true) -> "img_wedding_cards"
                        item.image.contains("mug", ignoreCase = true) -> "img_mugs"
                        item.image.contains("shirt", ignoreCase = true) -> "img_tshirts"
                        item.image.contains("sticker", ignoreCase = true) -> "img_stickers"
                        item.image.contains("id", ignoreCase = true) -> "img_id_cards"
                        else -> null
                    }
                }
            }
            if (fallbackName != null) {
                imageResId = context.resources.getIdentifier(fallbackName, "drawable", context.packageName)
            }
        }

        if (imageResId != 0) {
            holder.binding.imgGalleryThumb.setImageResource(imageResId)
            holder.binding.imgGalleryThumb.imageTintList = null
        } else {
            holder.binding.imgGalleryThumb.setImageResource(com.example.R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount(): Int = galleryItems.size

    fun updateData(newItems: List<GalleryItem>) {
        this.galleryItems = newItems
        notifyDataSetChanged()
    }
}
