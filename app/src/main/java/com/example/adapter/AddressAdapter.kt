package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Address
import com.example.databinding.ItemAddressBinding

class AddressAdapter(
    private var addresses: List<Address>,
    private val onDeleteClick: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.binding.txtAddressText.text = address.fullAddress
        holder.binding.txtAddressCity.text = "${address.city} • ${address.postalCode}"

        holder.binding.btnDeleteAddress.setOnClickListener {
            onDeleteClick(address)
        }
    }

    override fun getItemCount(): Int = addresses.size

    fun updateData(newAddresses: List<Address>) {
        this.addresses = newAddresses
        notifyDataSetChanged()
    }
}
