package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.model.Product
import com.example.model.Offer
import com.example.model.GalleryItem
import com.example.repository.ProductRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository = ProductRepository(application)

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> get() = _offers

    private val _galleryItems = MutableLiveData<List<GalleryItem>>()
    val galleryItems: LiveData<List<GalleryItem>> get() = _galleryItems

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> get() = _selectedProduct

    fun loadHomeData() {
        _products.value = productRepository.getAllProducts()
        _offers.value = productRepository.getAllOffers()
        _galleryItems.value = productRepository.getAllGalleryItems()
    }

    fun loadProductsByCategory(category: String) {
        _products.value = productRepository.getProductsByCategory(category)
    }

    fun loadProductDetails(productId: Int) {
        _selectedProduct.value = productRepository.getProductById(productId)
    }
}
