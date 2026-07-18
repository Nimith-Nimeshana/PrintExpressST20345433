package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.model.Order
import com.example.repository.OrderRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val orderRepository = OrderRepository(application)

    private val _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>> get() = _orderList

    private val _orderPlacementResult = MutableLiveData<Result<Boolean>>()
    val orderPlacementResult: LiveData<Result<Boolean>> get() = _orderPlacementResult

    private val _orderDetails = MutableLiveData<Order?>()
    val orderDetails: LiveData<Order?> get() = _orderDetails

    fun loadUserOrders(userId: Int) {
        _orderList.value = orderRepository.getOrdersForUser(userId)
    }

    fun placeNewOrder(
        userId: Int,
        productId: Int,
        quantity: Int,
        paperType: String,
        customText: String?,
        designPath: String?,
        deliveryMethod: String,
        totalPrice: Double
    ) {
        if (quantity <= 0) {
            _orderPlacementResult.value = Result.failure(Exception("Quantity must be greater than zero."))
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateString = dateFormat.format(Date())

        val success = orderRepository.placeOrder(
            userId = userId,
            productId = productId,
            quantity = quantity,
            paperType = paperType,
            customText = customText,
            designPath = designPath,
            deliveryMethod = deliveryMethod,
            totalPrice = totalPrice,
            date = currentDateString
        )

        if (success) {
            _orderPlacementResult.value = Result.success(true)
        } else {
            _orderPlacementResult.value = Result.failure(Exception("Failed to save order to local database."))
        }
    }

    fun loadOrderDetails(orderId: Int) {
        _orderDetails.value = orderRepository.getOrderDetails(orderId)
    }

    fun cancelOrder(orderId: Int) {
        val success = orderRepository.cancelOrder(orderId)
        if (success) {
            _orderDetails.value = orderRepository.getOrderDetails(orderId)
        }
    }
}
