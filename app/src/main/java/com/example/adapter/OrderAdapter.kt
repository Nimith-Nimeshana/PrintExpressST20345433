package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Order
import com.example.databinding.ItemOrderBinding
import com.example.utils.Constants

class OrderAdapter(
    private var orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.binding.txtOrderId.text = "Order #${order.orderId}"
        holder.binding.txtProductName.text = order.productName
        holder.binding.txtOrderDate.text = "Placed: ${order.date}"
        holder.binding.txtOrderQty.text = "Qty: ${order.quantity}"
        holder.binding.txtOrderPrice.text = "Rs. %.2f".format(order.totalPrice)
        holder.binding.txtOrderStatus.text = order.status

        // Dynamic badge color based on job status state
        val context = holder.itemView.context
        val colorRes = when (order.status) {
            Constants.STATUS_PENDING -> com.example.R.color.info
            Constants.STATUS_PROCESSING -> com.example.R.color.warning
            Constants.STATUS_PRINTING -> com.example.R.color.secondary
            Constants.STATUS_READY -> com.example.R.color.success
            Constants.STATUS_COMPLETED -> com.example.R.color.success
            Constants.STATUS_CANCELLED -> com.example.R.color.danger
            else -> com.example.R.color.info
        }
        holder.binding.cardStatusBadge.setCardBackgroundColor(context.getColor(colorRes))

        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<Order>) {
        this.orders = newOrders
        notifyDataSetChanged()
    }
}
