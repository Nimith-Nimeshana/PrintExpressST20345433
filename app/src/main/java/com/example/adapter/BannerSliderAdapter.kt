package com.example.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Offer
import com.example.databinding.ItemSliderBinding

class BannerSliderAdapter(
    private var offers: List<Offer>
) : RecyclerView.Adapter<BannerSliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val offer = offers[position]
        holder.binding.txtBannerTitle.text = offer.title
        holder.binding.txtBannerSubtitle.text = offer.description

        val context = holder.itemView.context

        // Dynamic Gradients Campaign Title
        val gradient = when {
            offer.title.contains("Avurudu", ignoreCase = true) || offer.title.contains("Awurudu", ignoreCase = true) -> {
                // Avurudu colors
                GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor("#E1251B"), // Traditional Avurudu Red
                        Color.parseColor("#FF6B00"), // Radiant Sun Orange
                        Color.parseColor("#FFB300")  // Auspicious Harvest Gold
                    )
                )
            }
            offer.title.contains("Christmas", ignoreCase = true) || offer.title.contains("Xmas", ignoreCase = true) -> {
                // Christmas colors
                GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor("#0F3E2B"), // Deep Pine Green
                        Color.parseColor("#1B6B4C"), // Vibrant Forest Green
                        Color.parseColor("#D4AF37")  // Festive Gold
                    )
                )
            }
            offer.title.contains("Corporate", ignoreCase = true) -> {
                // Corporate
                GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor("#0F172A"), // Slate Charcoal
                        Color.parseColor("#1E3A8A"), // Dark Navy
                        Color.parseColor("#3B82F6")  // Modern Royal Blue
                    )
                )
            }
            offer.title.contains("Student", ignoreCase = true) -> {
                // Student
                GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor("#4F46E5"), // Energetic Indigo
                        Color.parseColor("#7C3AED"), // Royal Purple
                        Color.parseColor("#EC4899")  // Creative Pink/Magenta
                    )
                )
            }
            else -> {
                // Default Brand theme gradient
                GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor("#0066FF"),
                        Color.parseColor("#0044CC")
                    )
                )
            }
        }

        // Apply corner radius to match card design
        val density = context.resources.displayMetrics.density
        gradient.cornerRadius = 14f * density
        holder.binding.layoutContainer.background = gradient

        // Set card icons
        val imageRes = when {
            offer.title.contains("Avurudu", ignoreCase = true) || offer.title.contains("Awurudu", ignoreCase = true) -> com.example.R.drawable.img_avurudu
            offer.title.contains("Christmas", ignoreCase = true) || offer.title.contains("Xmas", ignoreCase = true) -> com.example.R.drawable.img_chrismas
            offer.title.contains("Corporate", ignoreCase = true) -> com.example.R.drawable.img_id_cards
            offer.title.contains("Student", ignoreCase = true) -> com.example.R.drawable.img_students
            else -> com.example.R.drawable.ic_launcher_foreground
        }

        holder.binding.imgBanner.setImageResource(imageRes)

        // Clear the white color tint
        holder.binding.imgBanner.imageTintList = null

        // Set the Discount badge text and dynamic contrast text color
        holder.binding.txtBannerDiscount.text = offer.discount
        val badgeTextColor = when {
            offer.title.contains("Avurudu", ignoreCase = true) || offer.title.contains("Awurudu", ignoreCase = true) -> Color.parseColor("#E1251B") // Traditional Avurudu Red
            offer.title.contains("Christmas", ignoreCase = true) || offer.title.contains("Xmas", ignoreCase = true) -> Color.parseColor("#1B6B4C") // Forest Green
            offer.title.contains("Corporate", ignoreCase = true) -> Color.parseColor("#1E3A8A") // Dark Corporate Navy Blue
            offer.title.contains("Student", ignoreCase = true) -> Color.parseColor("#4F46E5") // Indigo/Purple
            else -> Color.parseColor("#0066FF") // Default brand Blue
        }
        holder.binding.txtBannerDiscount.setTextColor(badgeTextColor)
    }

    override fun getItemCount(): Int = offers.size

    fun updateData(newOffers: List<Offer>) {
        this.offers = newOffers
        notifyDataSetChanged()
    }
}
