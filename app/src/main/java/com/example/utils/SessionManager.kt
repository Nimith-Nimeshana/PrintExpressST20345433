package com.example.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        private const val PREF_NAME = "PrintXpressSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_USER_PHONE = "userPhone"
        private const val KEY_USER_ADDRESS = "userAddress"
        private const val KEY_REMEMBER_ME = "rememberMe"
    }

    fun createSession(userId: Int, name: String, email: String, phone: String, address: String?, rememberMe: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_PHONE, phone)
        editor.putString(KEY_USER_ADDRESS, address ?: "")
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun isRememberMe(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "User") ?: "User"
    }

    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getUserPhone(): String {
        return prefs.getString(KEY_USER_PHONE, "") ?: ""
    }

    fun getUserAddress(): String {
        return prefs.getString(KEY_USER_ADDRESS, "") ?: ""
    }

    fun updateProfileSession(name: String, phone: String, address: String) {
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_PHONE, phone)
        editor.putString(KEY_USER_ADDRESS, address)
        editor.apply()
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
