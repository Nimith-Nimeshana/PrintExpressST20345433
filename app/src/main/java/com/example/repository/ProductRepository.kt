package com.example.repository

import android.content.Context
import android.database.Cursor
import com.example.database.DatabaseHelper
import com.example.database.DatabaseContract.ProductsTable
import com.example.database.DatabaseContract.OffersTable
import com.example.database.DatabaseContract.GalleryTable
import com.example.model.Product
import com.example.model.Offer
import com.example.model.GalleryItem

class ProductRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ProductsTable.TABLE_NAME,
            null, null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRODUCT_ID)
            val catIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_CATEGORY)
            val nameIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_NAME)
            val descIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_DESCRIPTION)
            val matIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_MATERIAL)
            val sizeIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_SIZE)
            val priceIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRICE)
            val imgIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_IMAGE)

            do {
                list.add(
                    Product(
                        productId = cursor.getInt(idIdx),
                        category = cursor.getString(catIdx),
                        name = cursor.getString(nameIdx),
                        description = cursor.getString(descIdx),
                        material = cursor.getString(matIdx),
                        size = cursor.getString(sizeIdx),
                        price = cursor.getDouble(priceIdx),
                        image = cursor.getString(imgIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getProductsByCategory(category: String): List<Product> {
        val list = mutableListOf<Product>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ProductsTable.TABLE_NAME,
            null,
            "${ProductsTable.COLUMN_CATEGORY} = ?",
            arrayOf(category),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRODUCT_ID)
            val catIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_CATEGORY)
            val nameIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_NAME)
            val descIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_DESCRIPTION)
            val matIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_MATERIAL)
            val sizeIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_SIZE)
            val priceIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRICE)
            val imgIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_IMAGE)

            do {
                list.add(
                    Product(
                        productId = cursor.getInt(idIdx),
                        category = cursor.getString(catIdx),
                        name = cursor.getString(nameIdx),
                        description = cursor.getString(descIdx),
                        material = cursor.getString(matIdx),
                        size = cursor.getString(sizeIdx),
                        price = cursor.getDouble(priceIdx),
                        image = cursor.getString(imgIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getProductById(productId: Int): Product? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ProductsTable.TABLE_NAME,
            null,
            "${ProductsTable.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString()),
            null, null, null
        )

        var product: Product? = null
        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRODUCT_ID)
            val catIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_CATEGORY)
            val nameIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_NAME)
            val descIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_DESCRIPTION)
            val matIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_MATERIAL)
            val sizeIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_SIZE)
            val priceIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_PRICE)
            val imgIdx = cursor.getColumnIndexOrThrow(ProductsTable.COLUMN_IMAGE)

            product = Product(
                productId = cursor.getInt(idIdx),
                category = cursor.getString(catIdx),
                name = cursor.getString(nameIdx),
                description = cursor.getString(descIdx),
                material = cursor.getString(matIdx),
                size = cursor.getString(sizeIdx),
                price = cursor.getDouble(priceIdx),
                image = cursor.getString(imgIdx)
            )
        }
        cursor.close()
        return product
    }

    fun getAllOffers(): List<Offer> {
        val list = mutableListOf<Offer>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            OffersTable.TABLE_NAME,
            null, null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(OffersTable.COLUMN_OFFER_ID)
            val titleIdx = cursor.getColumnIndexOrThrow(OffersTable.COLUMN_TITLE)
            val descIdx = cursor.getColumnIndexOrThrow(OffersTable.COLUMN_DESCRIPTION)
            val discIdx = cursor.getColumnIndexOrThrow(OffersTable.COLUMN_DISCOUNT)

            do {
                list.add(
                    Offer(
                        offerId = cursor.getInt(idIdx),
                        title = cursor.getString(titleIdx),
                        description = cursor.getString(descIdx),
                        discount = cursor.getString(discIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllGalleryItems(): List<GalleryItem> {
        val list = mutableListOf<GalleryItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            GalleryTable.TABLE_NAME,
            null, null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(GalleryTable.COLUMN_GALLERY_ID)
            val titleIdx = cursor.getColumnIndexOrThrow(GalleryTable.COLUMN_TITLE)
            val imgIdx = cursor.getColumnIndexOrThrow(GalleryTable.COLUMN_IMAGE)

            do {
                list.add(
                    GalleryItem(
                        galleryId = cursor.getInt(idIdx),
                        title = cursor.getString(titleIdx),
                        image = cursor.getString(imgIdx)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
