package com.example.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.database.DatabaseHelper
import com.example.database.DatabaseContract.UsersTable
import com.example.database.DatabaseContract.AddressesTable
import com.example.model.User
import com.example.model.Address
import com.example.utils.HashUtils

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun registerUser(name: String, email: String, phone: String, passwordRaw: String): Boolean {
        val db = dbHelper.writableDatabase
        val passwordHash = HashUtils.sha256(passwordRaw)

        val values = ContentValues().apply {
            put(UsersTable.COLUMN_NAME, name)
            put(UsersTable.COLUMN_EMAIL, email)
            put(UsersTable.COLUMN_PHONE, phone)
            put(UsersTable.COLUMN_PASSWORD, passwordHash)
            put(UsersTable.COLUMN_ADDRESS, "")
        }

        val result = db.insert(UsersTable.TABLE_NAME, null, values)
        return result != -1L
    }

    fun isEmailExists(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UsersTable.TABLE_NAME,
            arrayOf(UsersTable.COLUMN_USER_ID),
            "${UsersTable.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun authenticateUser(email: String, passwordRaw: String): User? {
        val db = dbHelper.readableDatabase
        val passwordHash = HashUtils.sha256(passwordRaw)

        val cursor = db.query(
            UsersTable.TABLE_NAME,
            null,
            "${UsersTable.COLUMN_EMAIL} = ? AND ${UsersTable.COLUMN_PASSWORD} = ?",
            arrayOf(email, passwordHash),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_USER_ID)
            val nameIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_NAME)
            val emailIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_EMAIL)
            val phoneIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_PHONE)
            val addressIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_ADDRESS)

            user = User(
                userId = cursor.getInt(idIdx),
                name = cursor.getString(nameIdx),
                email = cursor.getString(emailIdx),
                phone = cursor.getString(phoneIdx),
                address = cursor.getString(addressIdx)
            )
        }
        cursor.close()
        return user
    }

    fun getUserById(userId: Int): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UsersTable.TABLE_NAME,
            null,
            "${UsersTable.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_USER_ID)
            val nameIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_NAME)
            val emailIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_EMAIL)
            val phoneIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_PHONE)
            val addressIdx = cursor.getColumnIndexOrThrow(UsersTable.COLUMN_ADDRESS)

            user = User(
                userId = cursor.getInt(idIdx),
                name = cursor.getString(nameIdx),
                email = cursor.getString(emailIdx),
                phone = cursor.getString(phoneIdx),
                address = cursor.getString(addressIdx)
            )
        }
        cursor.close()
        return user
    }

    fun updateUserProfile(userId: Int, name: String, phone: String, address: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UsersTable.COLUMN_NAME, name)
            put(UsersTable.COLUMN_PHONE, phone)
            put(UsersTable.COLUMN_ADDRESS, address)
        }
        val rows = db.update(
            UsersTable.TABLE_NAME,
            values,
            "${UsersTable.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString())
        )
        return rows > 0
    }

    // Addresses
    fun getAddressesForUser(userId: Int): List<Address> {
        val list = mutableListOf<Address>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AddressesTable.TABLE_NAME,
            null,
            "${AddressesTable.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(AddressesTable.COLUMN_ADDRESS_ID)
            val userIdx = cursor.getColumnIndexOrThrow(AddressesTable.COLUMN_USER_ID)
            val addrIdx = cursor.getColumnIndexOrThrow(AddressesTable.COLUMN_FULL_ADDRESS)
            val cityIdx = cursor.getColumnIndexOrThrow(AddressesTable.COLUMN_CITY)
            val pcIdx = cursor.getColumnIndexOrThrow(AddressesTable.COLUMN_POSTAL_CODE)

            do {
                list.add(
                    Address(
                        addressId = cursor.getInt(idIdx),
                        userId = cursor.getInt(userIdx),
                        fullAddress = cursor.getString(addrIdx),
                        city = cursor.getString(cityIdx),
                        postalCode = cursor.getString(pcIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun addAddress(userId: Int, fullAddress: String, city: String, postalCode: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AddressesTable.COLUMN_USER_ID, userId)
            put(AddressesTable.COLUMN_FULL_ADDRESS, fullAddress)
            put(AddressesTable.COLUMN_CITY, city)
            put(AddressesTable.COLUMN_POSTAL_CODE, postalCode)
        }
        val result = db.insert(AddressesTable.TABLE_NAME, null, values)
        return result != -1L
    }

    fun deleteAddress(addressId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            AddressesTable.TABLE_NAME,
            "${AddressesTable.COLUMN_ADDRESS_ID} = ?",
            arrayOf(addressId.toString())
        )
        return rows > 0
    }
}
