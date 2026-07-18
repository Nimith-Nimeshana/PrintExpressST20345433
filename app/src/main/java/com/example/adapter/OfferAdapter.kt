package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Offer
import com.example.databinding.ItemOfferBinding

class OfferAdapter(
    private var offers: List<Offer>
) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(val binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers[position]
        holder.binding.txtOfferTitle.text = offer.title
        holder.binding.txtOfferDesc.text = offer.description
        holder.binding.txtOfferDiscount.text = offer.discount
    }

    override fun getItemCount(): Int = offers.size

    fun updateData(newOffers: List<Offer>) {
        this.offers = newOffers
        notifyDataSetChanged()
    }
}
