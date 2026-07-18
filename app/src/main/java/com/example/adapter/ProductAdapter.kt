package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Product
import com.example.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.txtItemCategory.text = product.category
        holder.binding.txtItemTitle.text = product.name
        holder.binding.txtItemDesc.text = product.description
        holder.binding.txtItemPrice.text = "Rs. %.2f".format(product.price)

        // load product image by resource name string
        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
        if (imageResId != 0) {
            holder.binding.imgThumbnail.setImageResource(imageResId)
            holder.binding.imgThumbnail.imageTintList = null // Remove any tint to show full vector colors
        } else {
            holder.binding.imgThumbnail.setImageResource(com.example.R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}
