package com.example.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.MainActivity
import com.example.ProductDetailsActivity
import com.example.model.Product
import com.example.databinding.FragmentHomeBinding
import com.example.utils.SessionManager
import com.example.adapter.BannerSliderAdapter
import com.example.adapter.CategoryAdapter
import com.example.adapter.ProductAdapter
import com.example.utils.Constants
import com.example.viewmodel.HomeViewModel
import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var sessionManager: SessionManager

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var bannerAdapter: BannerSliderAdapter

    private var fullProductList = listOf<Product>()

    // Autosliding
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = object : Runnable {
        override fun run() {
            val count = bannerAdapter.itemCount
            if (count > 1) {
                val currentItem = binding.bannerViewPager.currentItem
                val nextItem = (currentItem + 1) % count
                binding.bannerViewPager.setCurrentItem(nextItem, true)
            }
            sliderHandler.postDelayed(this, 3000) // 3 seconds interval
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        // Set greetings
        binding.txtGreeting.text = "Welcome, ${sessionManager.getUserName()}!"

        setupRecyclerViews()
        observeViewModel()

        viewModel.loadHomeData()
    }

    private fun setupRecyclerViews() {
        // Categories
        val allCategories = listOf("All Products") + Constants.CATEGORIES
        categoryAdapter = CategoryAdapter(allCategories) { selectedCategory ->
            if (selectedCategory == "All Products") {
                viewModel.loadHomeData()
            } else {
                viewModel.loadProductsByCategory(selectedCategory)
            }
        }
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoryAdapter

        // Products
        productAdapter = ProductAdapter(listOf()) { product ->
            val intent = Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.productId)
            }
            startActivity(intent)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = productAdapter

        // Banner Auto Slider
        bannerAdapter = BannerSliderAdapter(listOf())
        binding.bannerViewPager.adapter = bannerAdapter

        // slide interactions to reset auto slide delay upon manual swipes
        binding.bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Search filtering
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterProducts(query: String) {
        if (query.isBlank()) {
            productAdapter.updateData(fullProductList)
        } else {
            val filtered = fullProductList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
            productAdapter.updateData(filtered)
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { productList ->
            fullProductList = productList
            productAdapter.updateData(productList)
        }

        viewModel.offers.observe(viewLifecycleOwner) { offersList ->
            bannerAdapter.updateData(offersList)
        }
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.removeCallbacks(sliderRunnable)
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
