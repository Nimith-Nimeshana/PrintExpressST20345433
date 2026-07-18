package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.model.Address
import com.example.model.SavedDesign
import com.example.model.User
import com.example.repository.UserRepository
import com.example.repository.OrderRepository
import com.example.utils.Validation

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val orderRepository = OrderRepository(application)

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    private val _addressList = MutableLiveData<List<Address>>()
    val addressList: LiveData<List<Address>> get() = _addressList

    private val _savedDesigns = MutableLiveData<List<SavedDesign>>()
    val savedDesigns: LiveData<List<SavedDesign>> get() = _savedDesigns

    private val _profileUpdateResult = MutableLiveData<Result<Boolean>>()
    val profileUpdateResult: LiveData<Result<Boolean>> get() = _profileUpdateResult

    private val _addressOperationResult = MutableLiveData<Result<Boolean>>()
    val addressOperationResult: LiveData<Result<Boolean>> get() = _addressOperationResult

    fun loadUserProfile(userId: Int) {
        val user = userRepository.getUserById(userId)
        _currentUser.value = user
    }

    fun updateProfile(userId: Int, name: String, phone: String, address: String) {
        if (!Validation.isNotEmpty(name)) {
            _profileUpdateResult.value = Result.failure(Exception("Name cannot be empty."))
            return
        }
        if (!Validation.isValidSriLankanPhone(phone)) {
            _profileUpdateResult.value = Result.failure(Exception("Please enter a valid Sri Lankan mobile number."))
            return
        }

        val success = userRepository.updateUserProfile(userId, name, phone, address)
        if (success) {
            _profileUpdateResult.value = Result.success(true)
            loadUserProfile(userId) // reload user profile
        } else {
            _profileUpdateResult.value = Result.failure(Exception("Failed to update profile."))
        }
    }

    fun loadAddresses(userId: Int) {
        _addressList.value = userRepository.getAddressesForUser(userId)
    }

    fun addAddress(userId: Int, fullAddress: String, city: String, postalCode: String) {
        if (fullAddress.isBlank() || city.isBlank() || postalCode.isBlank()) {
            _addressOperationResult.value = Result.failure(Exception("All address fields are required."))
            return
        }

        val success = userRepository.addAddress(userId, fullAddress, city, postalCode)
        if (success) {
            _addressOperationResult.value = Result.success(true)
            loadAddresses(userId)
        } else {
            _addressOperationResult.value = Result.failure(Exception("Failed to add address."))
        }
    }

    fun deleteAddress(userId: Int, addressId: Int) {
        val success = userRepository.deleteAddress(addressId)
        if (success) {
            _addressOperationResult.value = Result.success(true)
            loadAddresses(userId)
        } else {
            _addressOperationResult.value = Result.failure(Exception("Failed to delete address."))
        }
    }

    fun loadSavedDesigns(userId: Int) {
        _savedDesigns.value = orderRepository.getSavedDesigns(userId)
    }

    fun saveUserDesign(userId: Int, filePath: String) {
        val success = orderRepository.saveDesign(userId, filePath)
        if (success) {
            loadSavedDesigns(userId)
        }
    }

    fun deleteSavedDesign(userId: Int, designId: Int) {
        val success = orderRepository.deleteSavedDesign(designId)
        if (success) {
            loadSavedDesigns(userId)
        }
    }
}
