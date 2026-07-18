package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.model.User
import com.example.repository.UserRepository
import com.example.utils.Validation

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> get() = _loginResult

    private val _registerResult = MutableLiveData<Result<Boolean>>()
    val registerResult: LiveData<Result<Boolean>> get() = _registerResult

    fun login(email: String, passwordRaw: String) {
        if (!Validation.isValidEmail(email)) {
            _loginResult.value = Result.failure(Exception("Please enter a valid email address."))
            return
        }
        if (passwordRaw.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Please enter your password."))
            return
        }

        val user = userRepository.authenticateUser(email, passwordRaw)
        if (user != null) {
            _loginResult.value = Result.success(user)
        } else {
            _loginResult.value = Result.failure(Exception("Invalid email or password."))
        }
    }

    fun register(name: String, email: String, phone: String, passwordRaw: String, confirmPass: String) {
        if (!Validation.isNotEmpty(name)) {
            _registerResult.value = Result.failure(Exception("Name cannot be empty."))
            return
        }
        if (!Validation.isValidEmail(email)) {
            _registerResult.value = Result.failure(Exception("Please enter a valid email address."))
            return
        }
        if (!Validation.isValidSriLankanPhone(phone)) {
            _registerResult.value = Result.failure(Exception("Please enter a valid Sri Lankan mobile number (e.g. 0771234567)."))
            return
        }
        if (!Validation.isValidPassword(passwordRaw)) {
            _registerResult.value = Result.failure(Exception("Password must be at least 8 characters and include uppercase, lowercase, and a digit."))
            return
        }
        if (!Validation.doPasswordsMatch(passwordRaw, confirmPass)) {
            _registerResult.value = Result.failure(Exception("Passwords do not match."))
            return
        }

        if (userRepository.isEmailExists(email)) {
            _registerResult.value = Result.failure(Exception("An account with this email already exists."))
            return
        }

        val success = userRepository.registerUser(name, email, phone, passwordRaw)
        if (success) {
            _registerResult.value = Result.success(true)
        } else {
            _registerResult.value = Result.failure(Exception("Registration failed. Please try again."))
        }
    }
}
