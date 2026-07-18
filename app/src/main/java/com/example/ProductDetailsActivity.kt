package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityProductDetailsBinding
import com.example.viewmodel.HomeViewModel
import java.io.File
import java.io.FileOutputStream

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private val viewModel: HomeViewModel by viewModels()

    private var productId: Int = -1
    private var baseUnitPrice: Double = 0.0
    private var selectedFilePath: String? = null

    // Register file picker
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            copyFileToInternalStorage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        productId = intent.getIntExtra("PRODUCT_ID", -1)

        setupSpinners()
        setupListeners()
        observeViewModel()

        viewModel.loadProductDetails(productId)
    }

    private fun setupSpinners() {
        val paperStocks = arrayOf(
            "Standard Matte",
            "Premium Glossy",
            "Linen Textured Card",
            "Glossy (Flyer weight)",
            "Weatherproof Flex Banner Canvas"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paperStocks)
        binding.spinnerPaperType.adapter = adapter
    }

    private fun setupListeners() {
        // Dynamic price estimation on quantity changes in real-time
        binding.etQuantity.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                calculateTotalEstimate()
            }
        })

        binding.btnPickFile.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        binding.btnCheckout.setOnClickListener {
            proceedToCheckout()
        }
    }

    private fun calculateTotalEstimate() {
        val qtyText = binding.etQuantity.text.toString()
        val quantity = if (qtyText.isNotEmpty()) qtyText.toInt() else 0
        val estimate = quantity * baseUnitPrice
        binding.txtTotalEstimate.text = "Rs. %.2f".format(estimate)
    }

    private fun copyFileToInternalStorage(uri: Uri) {
        try {
            val contentResolver = contentResolver
            // Try to find filename
            var fileName = "custom_artwork_design.pdf"
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }

            val targetDir = File(filesDir, "saved_designs")
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }

            val targetFile = File(targetDir, fileName)
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            selectedFilePath = targetFile.absolutePath
            binding.txtFilePath.text = fileName
            Toast.makeText(this, "Artwork Selected Successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to Copy Artwork: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun proceedToCheckout() {
        val qtyText = binding.etQuantity.text.toString().trim()
        if (qtyText.isEmpty()) {
            Toast.makeText(this, "Please Specify Order Quantity.", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = qtyText.toInt()
        if (quantity <= 0) {
            Toast.makeText(this, "Select Any Quantity!", Toast.LENGTH_SHORT).show()
            return
        }

        val paperType = binding.spinnerPaperType.selectedItem.toString()
        val customText = binding.etCustomText.text.toString().trim()

        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putExtra("PRODUCT_ID", productId)
            putExtra("QUANTITY", quantity)
            putExtra("PAPER_TYPE", paperType)
            putExtra("CUSTOM_TEXT", customText)
            putExtra("DESIGN_PATH", selectedFilePath)
            putExtra("PRICE", baseUnitPrice)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.selectedProduct.observe(this) { product ->
            product?.let {
                binding.txtProductName.text = it.name
                binding.txtProductDesc.text = it.description
                binding.txtBaseMaterial.text = it.material
                binding.txtBaseSize.text = it.size
                baseUnitPrice = it.price
                binding.txtUnitPrice.text = "Rs. %.2f".format(it.price)

                // product image resource
                val imageResId = resources.getIdentifier(it.image, "drawable", packageName)
                if (imageResId != 0) {
                    binding.imgProduct.setImageResource(imageResId)
                    binding.imgProduct.imageTintList = null // Clear default XML primary tint
                } else {
                    binding.imgProduct.setImageResource(R.drawable.ic_launcher_foreground)
                }

                // adjust dropdown based on category
                updateCustomDropdown(it.category)

                calculateTotalEstimate()
            }
        }
    }

    private fun updateCustomDropdown(category: String) {
        val labelText: String
        val options: Array<String>

        when (category.trim()) {
            "Business Cards" -> {
                labelText = "Paper / Stock Selection"
                options = arrayOf(
                    "Standard Matte",
                    "Premium Glossy",
                    "Linen Textured Card",
                    "Premium Velvet Touch"
                )
            }
            "Flyers" -> {
                labelText = "Paper / Stock Selection"
                options = arrayOf(
                    "Standard Glossy",
                    "Premium Matte",
                    "Eco-Friendly Recycled Paper",
                    "Silk Coated Art Paper"
                )
            }
            "Posters" -> {
                labelText = "Paper / Stock Selection"
                options = arrayOf(
                    "High Gloss Photo Paper",
                    "Matte Coated Art Paper",
                    "Heavy Premium Poster Board",
                    "Satin Finish Photo Paper"
                )
            }
            "Banners" -> {
                labelText = "Material & Finishing Selection"
                options = arrayOf(
                    "Heavy-Duty Flex Canvas",
                    "Premium Windproof Mesh Banner",
                    "Eco-Solvent Backlit Film",
                    "Satin Fabric Polyester Banner"
                )
            }
            "Stickers" -> {
                labelText = "Sticker Material Selection"
                options = arrayOf(
                    "Glossy Waterproof Vinyl",
                    "Matte Finish Die-Cut Vinyl",
                    "Transparent Clear Sticker Film",
                    "Textured Eco Kraft Paper"
                )
            }
            "Mugs" -> {
                labelText = "Mug Type & Style Selection"
                options = arrayOf(
                    "Ceramic White (Standard 11oz)",
                    "Magic Color-Changing (Heat Sensitive)",
                    "Frosted Glass Matte Mug",
                    "Inner-Color Accent Mug"
                )
            }
            "T-Shirts" -> {
                labelText = "Fabric & Blend Selection"
                options = arrayOf(
                    "100% Ring-Spun Cotton",
                    "Ultra-Soft Tri-Blend Cotton-Poly-Rayon",
                    "Polyester Active-Dry Sports Fabric",
                    "Premium Organic Bamboo Fiber"
                )
            }
            "Wedding Cards" -> {
                labelText = "Elegant Paper Selection"
                options = arrayOf(
                    "Textured Linen Card",
                    "Metallic Shimmer Pearl Card",
                    "Premium Cotton Rag Card",
                    "Elegant Vellum Overlay Card"
                )
            }
            else -> {
                labelText = "Paper / Stock Selection"
                options = arrayOf(
                    "Standard Matte",
                    "Premium Glossy",
                    "Linen Textured Card",
                    "Glossy (Flyer weight)",
                    "Weatherproof Flex Banner Canvas"
                )
            }
        }

        binding.lblPaperType.text = labelText
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        binding.spinnerPaperType.adapter = adapter
    }
}
