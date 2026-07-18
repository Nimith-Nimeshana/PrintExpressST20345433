package com.example.utils

import android.util.Patterns

object Validation {

    /**
     * Validates that a string is not empty.
     */
    fun isNotEmpty(text: String?): Boolean {
        return !text.isNullOrBlank()
    }

    /**
     * Validates that an email is right format
     */
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Validate mobile number format.
     */
    fun isValidSriLankanPhone(phone: String?): Boolean {
        if (phone.isNullOrBlank()) return false
        val regex = "^(0|\\+94|94)?7[0-9]{8}$".toRegex()
        return regex.matches(phone)
    }

    /**
     * Validates the password
     */
    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) return false
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    /**
     * Confirms that passwords match
     */
    fun doPasswordsMatch(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }
}
