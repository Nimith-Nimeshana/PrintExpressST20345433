package com.example.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.database.DatabaseHelper
import com.example.database.DatabaseContract.OrdersTable
import com.example.database.DatabaseContract.ProductsTable
import com.example.database.DatabaseContract.SavedDesignsTable
import com.example.model.Order
import com.example.model.SavedDesign

class OrderRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun placeOrder(
        userId: Int,
        productId: Int,
        quantity: Int,
        paperType: String,
        customText: String?,
        designPath: String?,
        deliveryMethod: String,
        totalPrice: Double,
        date: String
    ): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(OrdersTable.COLUMN_USER_ID, userId)
            put(OrdersTable.COLUMN_PRODUCT_ID, productId)
            put(OrdersTable.COLUMN_QUANTITY, quantity)
            put(OrdersTable.COLUMN_PAPER_TYPE, paperType)
            put(OrdersTable.COLUMN_CUSTOM_TEXT, customText)
            put(OrdersTable.COLUMN_DESIGN_PATH, designPath)
            put(OrdersTable.COLUMN_DELIVERY_METHOD, deliveryMethod)
            put(OrdersTable.COLUMN_STATUS, "Pending")
            put(OrdersTable.COLUMN_DATE, date)
            put(OrdersTable.COLUMN_TOTAL_PRICE, totalPrice)
        }
        val result = db.insert(OrdersTable.TABLE_NAME, null, values)
        return result != -1L
    }

    fun getOrdersForUser(userId: Int): List<Order> {
        val list = mutableListOf<Order>()
        val db = dbHelper.readableDatabase

        // Join Orders with Products to fetch ProductName and ProductImage
        val query = """
            SELECT o.*, p.${ProductsTable.COLUMN_NAME} AS prod_name, p.${ProductsTable.COLUMN_IMAGE} AS prod_image
            FROM ${OrdersTable.TABLE_NAME} o
            JOIN ${ProductsTable.TABLE_NAME} p ON o.${OrdersTable.COLUMN_PRODUCT_ID} = p.${ProductsTable.COLUMN_PRODUCT_ID}
            WHERE o.${OrdersTable.COLUMN_USER_ID} = ?
            ORDER BY o.${OrdersTable.COLUMN_ORDER_ID} DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_ORDER_ID)
            val userIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_USER_ID)
            val prodIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_PRODUCT_ID)
            val qtyIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_QUANTITY)
            val paperIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_PAPER_TYPE)
            val textIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_CUSTOM_TEXT)
            val designIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DESIGN_PATH)
            val delIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DELIVERY_METHOD)
            val statusIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_STATUS)
            val dateIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DATE)
            val priceIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_TOTAL_PRICE)
            val prodNameIdx = cursor.getColumnIndexOrThrow("prod_name")
            val prodImgIdx = cursor.getColumnIndexOrThrow("prod_image")

            do {
                val order = Order(
                    orderId = cursor.getInt(idIdx),
                    userId = cursor.getInt(userIdx),
                    productId = cursor.getInt(prodIdx),
                    quantity = cursor.getInt(qtyIdx),
                    paperType = cursor.getString(paperIdx),
                    customText = cursor.getString(textIdx),
                    designPath = cursor.getString(designIdx),
                    deliveryMethod = cursor.getString(delIdx),
                    status = cursor.getString(statusIdx),
                    date = cursor.getString(dateIdx),
                    totalPrice = cursor.getDouble(priceIdx),
                    productName = cursor.getString(prodNameIdx),
                    productImage = cursor.getString(prodImgIdx)
                )
                list.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getOrderDetails(orderId: Int): Order? {
        val db = dbHelper.readableDatabase

        val query = """
            SELECT o.*, p.${ProductsTable.COLUMN_NAME} AS prod_name, p.${ProductsTable.COLUMN_IMAGE} AS prod_image
            FROM ${OrdersTable.TABLE_NAME} o
            JOIN ${ProductsTable.TABLE_NAME} p ON o.${OrdersTable.COLUMN_PRODUCT_ID} = p.${ProductsTable.COLUMN_PRODUCT_ID}
            WHERE o.${OrdersTable.COLUMN_ORDER_ID} = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(orderId.toString()))

        var order: Order? = null
        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_ORDER_ID)
            val userIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_USER_ID)
            val prodIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_PRODUCT_ID)
            val qtyIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_QUANTITY)
            val paperIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_PAPER_TYPE)
            val textIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_CUSTOM_TEXT)
            val designIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DESIGN_PATH)
            val delIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DELIVERY_METHOD)
            val statusIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_STATUS)
            val dateIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_DATE)
            val priceIdx = cursor.getColumnIndexOrThrow(OrdersTable.COLUMN_TOTAL_PRICE)
            val prodNameIdx = cursor.getColumnIndexOrThrow("prod_name")
            val prodImgIdx = cursor.getColumnIndexOrThrow("prod_image")

            order = Order(
                orderId = cursor.getInt(idIdx),
                userId = cursor.getInt(userIdx),
                productId = cursor.getInt(prodIdx),
                quantity = cursor.getInt(qtyIdx),
                paperType = cursor.getString(paperIdx),
                customText = cursor.getString(textIdx),
                designPath = cursor.getString(designIdx),
                deliveryMethod = cursor.getString(delIdx),
                status = cursor.getString(statusIdx),
                date = cursor.getString(dateIdx),
                totalPrice = cursor.getDouble(priceIdx),
                productName = cursor.getString(prodNameIdx),
                productImage = cursor.getString(prodImgIdx)
            )
        }
        cursor.close()
        return order
    }

    // Saved Designs Support
    fun saveDesign(userId: Int, filePath: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SavedDesignsTable.COLUMN_USER_ID, userId)
            put(SavedDesignsTable.COLUMN_FILE_PATH, filePath)
        }
        val result = db.insert(SavedDesignsTable.TABLE_NAME, null, values)
        return result != -1L
    }

    fun getSavedDesigns(userId: Int): List<SavedDesign> {
        val list = mutableListOf<SavedDesign>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SavedDesignsTable.TABLE_NAME,
            null,
            "${SavedDesignsTable.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(SavedDesignsTable.COLUMN_DESIGN_ID)
            val userIdx = cursor.getColumnIndexOrThrow(SavedDesignsTable.COLUMN_USER_ID)
            val pathIdx = cursor.getColumnIndexOrThrow(SavedDesignsTable.COLUMN_FILE_PATH)

            do {
                list.add(
                    SavedDesign(
                        designId = cursor.getInt(idIdx),
                        userId = cursor.getInt(userIdx),
                        filePath = cursor.getString(pathIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun deleteSavedDesign(designId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            SavedDesignsTable.TABLE_NAME,
            "${SavedDesignsTable.COLUMN_DESIGN_ID} = ?",
            arrayOf(designId.toString())
        )
        return rows > 0
    }

    fun cancelOrder(orderId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(OrdersTable.COLUMN_STATUS, "Cancelled")
        }
        val rows = db.update(
            OrdersTable.TABLE_NAME,
            values,
            "${OrdersTable.COLUMN_ORDER_ID} = ?",
            arrayOf(orderId.toString())
        )
        return rows > 0
    }
}
