package com.example

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityOrderDetailsBinding
import com.example.utils.Constants
import com.example.viewmodel.OrdersViewModel
import java.io.File

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val orderId = intent.getIntExtra("ORDER_ID", -1)

        observeViewModel()
        viewModel.loadOrderDetails(orderId)
    }

    private fun observeViewModel() {
        viewModel.orderDetails.observe(this) { order ->
            order?.let {
                binding.txtOrderId.text = "Order #${it.orderId}"
                binding.txtOrderDate.text = "Placed: ${it.date}"

                binding.txtSummaryTitle.text = it.productName
                val fileName = if (it.designPath != null) File(it.designPath).name else "None"
                binding.txtSummaryDetails.text = """
                    Selected Option: ${it.paperType}
                    Quantity: ${it.quantity} prints
                    Guidelines Text: '${it.customText ?: "None"}'
                    Design File: $fileName
                    Delivery: ${it.deliveryMethod} flat-rate
                """.trimIndent()

                binding.txtSummaryPrice.text = "Rs. %.2f".format(it.totalPrice)

                updateTimelineStatus(it.status)

                // Manage Cancel Button Visibility & Action
                if (it.status == Constants.STATUS_PENDING) {
                    binding.btnCancelOrder.visibility = View.VISIBLE
                    binding.btnCancelOrder.setOnClickListener {
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Cancel Order")
                            .setMessage("Are You Sure to Cancel the Order?")
                            .setPositiveButton("Yes") { _, _ ->
                                viewModel.cancelOrder(order.orderId)
                                android.widget.Toast.makeText(this, "Order Cancelled Successfully", android.widget.Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No", null)
                            .show()
                    }
                } else {
                    binding.btnCancelOrder.visibility = View.GONE
                }
            }
        }
    }

    private fun updateTimelineStatus(status: String) {
        val activeColor = getColor(R.color.success)
        val activeText = getColor(R.color.text_primary)
        val inactiveColor = getColor(R.color.text_hint)
        val inactiveText = getColor(R.color.text_secondary)

        // Reset Timeline colors
        binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        binding.txtStepPending.setTextColor(inactiveText)
        binding.lineStep1.setBackgroundColor(inactiveColor)

        binding.iconStepProcessing.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        binding.txtStepProcessing.setTextColor(inactiveText)
        binding.lineStep2.setBackgroundColor(inactiveColor)

        binding.iconStepPrinting.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        binding.txtStepPrinting.setTextColor(inactiveText)
        binding.lineStep3.setBackgroundColor(inactiveColor)

        binding.iconStepReady.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        binding.txtStepReady.setTextColor(inactiveText)
        binding.lineStep4.setBackgroundColor(inactiveColor)

        binding.iconStepCompleted.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        binding.txtStepCompleted.setTextColor(inactiveText)

        // Highlight based on current state (and cascade downwards)
        when (status) {
            Constants.STATUS_PENDING -> {
                binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPending.setTextColor(activeText)
            }
            Constants.STATUS_PROCESSING -> {
                binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPending.setTextColor(activeText)
                binding.lineStep1.setBackgroundColor(activeColor)

                binding.iconStepProcessing.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepProcessing.setTextColor(activeText)
            }
            Constants.STATUS_PRINTING -> {
                binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPending.setTextColor(activeText)
                binding.lineStep1.setBackgroundColor(activeColor)

                binding.iconStepProcessing.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepProcessing.setTextColor(activeText)
                binding.lineStep2.setBackgroundColor(activeColor)

                binding.iconStepPrinting.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPrinting.setTextColor(activeText)
            }
            Constants.STATUS_READY -> {
                binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPending.setTextColor(activeText)
                binding.lineStep1.setBackgroundColor(activeColor)

                binding.iconStepProcessing.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepProcessing.setTextColor(activeText)
                binding.lineStep2.setBackgroundColor(activeColor)

                binding.iconStepPrinting.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPrinting.setTextColor(activeText)
                binding.lineStep3.setBackgroundColor(activeColor)

                binding.iconStepReady.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepReady.setTextColor(activeText)
            }
            Constants.STATUS_COMPLETED -> {
                binding.iconStepPending.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPending.setTextColor(activeText)
                binding.lineStep1.setBackgroundColor(activeColor)

                binding.iconStepProcessing.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepProcessing.setTextColor(activeText)
                binding.lineStep2.setBackgroundColor(activeColor)

                binding.iconStepPrinting.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepPrinting.setTextColor(activeText)
                binding.lineStep3.setBackgroundColor(activeColor)

                binding.iconStepReady.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepReady.setTextColor(activeText)
                binding.lineStep4.setBackgroundColor(activeColor)

                binding.iconStepCompleted.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                binding.txtStepCompleted.setTextColor(activeText)
            }
        }
    }
}
