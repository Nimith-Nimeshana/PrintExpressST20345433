package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<String>,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedIndex = 0

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.txtCategoryName.text = category

        // Simple selection indicator state style
        if (position == selectedIndex) {
            holder.binding.cardCategory.setCardBackgroundColor(
                holder.itemView.context.getColor(com.example.R.color.primary)
            )
        } else {
            holder.binding.cardCategory.setCardBackgroundColor(
                holder.itemView.context.getColor(com.example.R.color.card_dark)
            )
        }

        holder.itemView.setOnClickListener {
            val oldIndex = selectedIndex
            selectedIndex = holder.adapterPosition
            notifyItemChanged(oldIndex)
            notifyItemChanged(selectedIndex)
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}
