package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityCheckoutBinding
import com.example.utils.SessionManager
import com.example.viewmodel.OrdersViewModel
import com.example.viewmodel.ProfileViewModel
import java.io.File

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val ordersViewModel: OrdersViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    private var productId: Int = -1
    private var quantity: Int = 100
    private var paperType: String = ""
    private var customText: String? = null
    private var designPath: String? = null
    private var unitPrice: Double = 0.0
    private var subtotal: Double = 0.0
    private var deliveryFee: Double = 0.0
    private var totalPayable: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        sessionManager = SessionManager(this)

        // Read configuration extras
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        quantity = intent.getIntExtra("QUANTITY", 100)
        paperType = intent.getStringExtra("PAPER_TYPE") ?: "Standard Matte"
        customText = intent.getStringExtra("CUSTOM_TEXT")
        designPath = intent.getStringExtra("DESIGN_PATH")
        unitPrice = intent.getDoubleExtra("PRICE", 0.0)

        subtotal = quantity * unitPrice

        setupOrderReview()
        setupListeners()
        observeViewModel()

        // Fetch shipping address if any exists
        profileViewModel.loadUserProfile(sessionManager.getUserId())
        profileViewModel.loadAddresses(sessionManager.getUserId())
    }

    private fun setupOrderReview() {
        binding.txtItemSummary.text = "Item Configuration #$productId"
        val fileName = if (designPath != null) File(designPath!!).name else "None"
        binding.txtSpecsSummary.text = """
            Quantity: $quantity
            Selected Option: $paperType
            Branding Info: '${customText ?: "None"}'
            Design File: $fileName
        """.trimIndent()

        binding.txtSubtotal.text = "Rs. %.2f".format(subtotal)
        binding.txtBreakdownSubtotal.text = "Rs. %.2f".format(subtotal)
        calculateTotalBill()
    }

    private fun calculateTotalBill() {
        deliveryFee = if (binding.rbDelivery.isChecked) 350.00 else 0.00
        totalPayable = subtotal + deliveryFee

        binding.txtBreakdownDelivery.text = "Rs. %.2f".format(deliveryFee)
        binding.txtBreakdownTotal.text = "Rs. %.2f".format(totalPayable)
    }

    private fun setupListeners() {
        binding.rgDelivery.setOnCheckedChangeListener { _, _ ->
            if (binding.rbDelivery.isChecked) {
                binding.layoutShippingAddress.visibility = View.VISIBLE
            } else {
                binding.layoutShippingAddress.visibility = View.GONE
            }
            calculateTotalBill()
        }

        binding.btnChangeAddress.setOnClickListener {
            startActivity(Intent(this, ManageAddressesActivity::class.java))
        }

        binding.btnCompleteOrder.setOnClickListener {
            completePrintOrder()
        }
    }

    private fun completePrintOrder() {
        val deliveryMethod = if (binding.rbDelivery.isChecked) "Delivery" else "Pickup"

        if (deliveryMethod == "Delivery" && profileViewModel.addressList.value.isNullOrEmpty()) {
            Toast.makeText(this, "Please Add a Delivery Address to Proceed.", Toast.LENGTH_LONG).show()
            return
        }

        ordersViewModel.placeNewOrder(
            userId = sessionManager.getUserId(),
            productId = productId,
            quantity = quantity,
            paperType = paperType,
            customText = customText,
            designPath = designPath,
            deliveryMethod = deliveryMethod,
            totalPrice = totalPayable
        )
    }

    private fun observeViewModel() {
        profileViewModel.currentUser.observe(this) { user ->
            user?.let {
                if (it.address.isNullOrBlank()) {
                    binding.txtShippingAddress.text = "No Default Profile Address Set Yet."
                } else {
                    binding.txtShippingAddress.text = it.address
                }
            }
        }

        profileViewModel.addressList.observe(this) { addresses ->
            if (!addresses.isEmpty()) {
                val primary = addresses[0]
                binding.txtShippingAddress.text = "${primary.fullAddress}, ${primary.city} [Postal Code: ${primary.postalCode}]"
            }
        }

        ordersViewModel.orderPlacementResult.observe(this) { result ->
            result.onSuccess {
                // Save design to history lists on completion
                designPath?.let { path ->
                    profileViewModel.saveUserDesign(sessionManager.getUserId(), path)
                }

                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show()

                // Redirect to Orders Tab
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, "Order failure: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
