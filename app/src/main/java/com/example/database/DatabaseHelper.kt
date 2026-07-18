package com.example.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.database.DatabaseContract.UsersTable
import com.example.database.DatabaseContract.ProductsTable
import com.example.database.DatabaseContract.OrdersTable
import com.example.database.DatabaseContract.OffersTable
import com.example.database.DatabaseContract.GalleryTable
import com.example.database.DatabaseContract.SavedDesignsTable
import com.example.database.DatabaseContract.AddressesTable
import com.example.utils.HashUtils

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "DatabaseHelper"
        const val DATABASE_NAME = "printxpress.db"
        const val DATABASE_VERSION = 5
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating database tables...")

        // Users Table
        val createUsersTable = """
            CREATE TABLE ${UsersTable.TABLE_NAME} (
                ${UsersTable.COLUMN_USER_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${UsersTable.COLUMN_NAME} TEXT NOT NULL,
                ${UsersTable.COLUMN_EMAIL} TEXT UNIQUE NOT NULL,
                ${UsersTable.COLUMN_PHONE} TEXT NOT NULL,
                ${UsersTable.COLUMN_PASSWORD} TEXT NOT NULL,
                ${UsersTable.COLUMN_ADDRESS} TEXT
            )
        """.trimIndent()

        // Products Table
        val createProductsTable = """
            CREATE TABLE ${ProductsTable.TABLE_NAME} (
                ${ProductsTable.COLUMN_PRODUCT_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${ProductsTable.COLUMN_CATEGORY} TEXT NOT NULL,
                ${ProductsTable.COLUMN_NAME} TEXT NOT NULL,
                ${ProductsTable.COLUMN_DESCRIPTION} TEXT NOT NULL,
                ${ProductsTable.COLUMN_MATERIAL} TEXT NOT NULL,
                ${ProductsTable.COLUMN_SIZE} TEXT NOT NULL,
                ${ProductsTable.COLUMN_PRICE} REAL NOT NULL,
                ${ProductsTable.COLUMN_IMAGE} TEXT NOT NULL
            )
        """.trimIndent()

        // Orders Table
        val createOrdersTable = """
            CREATE TABLE ${OrdersTable.TABLE_NAME} (
                ${OrdersTable.COLUMN_ORDER_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${OrdersTable.COLUMN_USER_ID} INTEGER NOT NULL,
                ${OrdersTable.COLUMN_PRODUCT_ID} INTEGER NOT NULL,
                ${OrdersTable.COLUMN_QUANTITY} INTEGER NOT NULL,
                ${OrdersTable.COLUMN_PAPER_TYPE} TEXT NOT NULL,
                ${OrdersTable.COLUMN_CUSTOM_TEXT} TEXT,
                ${OrdersTable.COLUMN_DESIGN_PATH} TEXT,
                ${OrdersTable.COLUMN_DELIVERY_METHOD} TEXT NOT NULL,
                ${OrdersTable.COLUMN_STATUS} TEXT NOT NULL,
                ${OrdersTable.COLUMN_DATE} TEXT NOT NULL,
                ${OrdersTable.COLUMN_TOTAL_PRICE} REAL NOT NULL,
                FOREIGN KEY (${OrdersTable.COLUMN_USER_ID}) REFERENCES ${UsersTable.TABLE_NAME}(${UsersTable.COLUMN_USER_ID}),
                FOREIGN KEY (${OrdersTable.COLUMN_PRODUCT_ID}) REFERENCES ${ProductsTable.TABLE_NAME}(${ProductsTable.COLUMN_PRODUCT_ID})
            )
        """.trimIndent()

        // Offers Table
        val createOffersTable = """
            CREATE TABLE ${OffersTable.TABLE_NAME} (
                ${OffersTable.COLUMN_OFFER_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${OffersTable.COLUMN_TITLE} TEXT NOT NULL,
                ${OffersTable.COLUMN_DESCRIPTION} TEXT NOT NULL,
                ${OffersTable.COLUMN_DISCOUNT} TEXT NOT NULL
            )
        """.trimIndent()

        // Gallery Table
        val createGalleryTable = """
            CREATE TABLE ${GalleryTable.TABLE_NAME} (
                ${GalleryTable.COLUMN_GALLERY_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${GalleryTable.COLUMN_TITLE} TEXT NOT NULL,
                ${GalleryTable.COLUMN_IMAGE} TEXT NOT NULL
            )
        """.trimIndent()

        // Saved Designs Table
        val createSavedDesignsTable = """
            CREATE TABLE ${SavedDesignsTable.TABLE_NAME} (
                ${SavedDesignsTable.COLUMN_DESIGN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${SavedDesignsTable.COLUMN_USER_ID} INTEGER NOT NULL,
                ${SavedDesignsTable.COLUMN_FILE_PATH} TEXT NOT NULL,
                FOREIGN KEY (${SavedDesignsTable.COLUMN_USER_ID}) REFERENCES ${UsersTable.TABLE_NAME}(${UsersTable.COLUMN_USER_ID})
            )
        """.trimIndent()

        // Addresses Table
        val createAddressesTable = """
            CREATE TABLE ${AddressesTable.TABLE_NAME} (
                ${AddressesTable.COLUMN_ADDRESS_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${AddressesTable.COLUMN_USER_ID} INTEGER NOT NULL,
                ${AddressesTable.COLUMN_FULL_ADDRESS} TEXT NOT NULL,
                ${AddressesTable.COLUMN_CITY} TEXT NOT NULL,
                ${AddressesTable.COLUMN_POSTAL_CODE} TEXT NOT NULL,
                FOREIGN KEY (${AddressesTable.COLUMN_USER_ID}) REFERENCES ${UsersTable.TABLE_NAME}(${UsersTable.COLUMN_USER_ID})
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createProductsTable)
        db.execSQL(createOrdersTable)
        db.execSQL(createOffersTable)
        db.execSQL(createGalleryTable)
        db.execSQL(createSavedDesignsTable)
        db.execSQL(createAddressesTable)

        // Seed initial data
        seedProducts(db)
        seedOffers(db)
        seedGallery(db)
        seedDemoUser(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${AddressesTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${SavedDesignsTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${GalleryTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${OffersTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${OrdersTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${ProductsTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${UsersTable.TABLE_NAME}")
        onCreate(db)
    }

    private fun seedProducts(db: SQLiteDatabase) {
        val products = listOf(
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Business Cards")
                put(ProductsTable.COLUMN_NAME, "Premium Corporate Business Cards")
                put(ProductsTable.COLUMN_DESCRIPTION, "Make a lasting first impression with professional, sleek corporate cards.")
                put(ProductsTable.COLUMN_MATERIAL, "Premium Glossy 400gsm")
                put(ProductsTable.COLUMN_SIZE, "Standard (3.5\" x 2.0\")")
                put(ProductsTable.COLUMN_PRICE, 15.00)
                put(ProductsTable.COLUMN_IMAGE, "img_business_cards")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Flyers")
                put(ProductsTable.COLUMN_NAME, "Promotional Marketing Flyers")
                put(ProductsTable.COLUMN_DESCRIPTION, "Bright and glossy promotional flyers, perfect for distribution in Sri Lanka.")
                put(ProductsTable.COLUMN_MATERIAL, "Glossy 150gsm")
                put(ProductsTable.COLUMN_SIZE, "A5 Standard")
                put(ProductsTable.COLUMN_PRICE, 35.00)
                put(ProductsTable.COLUMN_IMAGE, "img_flyers")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Posters")
                put(ProductsTable.COLUMN_NAME, "High-Impact Event Posters")
                put(ProductsTable.COLUMN_DESCRIPTION, "Durable, high-color density posters designed to captivate your local audience.")
                put(ProductsTable.COLUMN_MATERIAL, "High Gloss Photo Paper 220gsm")
                put(ProductsTable.COLUMN_SIZE, "A3 Poster Size")
                put(ProductsTable.COLUMN_PRICE, 180.00)
                put(ProductsTable.COLUMN_IMAGE, "img_posters")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Banners")
                put(ProductsTable.COLUMN_NAME, "Outdoor Flex Exhibition Banners")
                put(ProductsTable.COLUMN_DESCRIPTION, "Weatherproof flex banners with metal eyelets for outdoor displays.")
                put(ProductsTable.COLUMN_MATERIAL, "Heavy Flex Canvas")
                put(ProductsTable.COLUMN_SIZE, "6ft x 3ft Banner")
                put(ProductsTable.COLUMN_PRICE, 1500.00)
                put(ProductsTable.COLUMN_IMAGE, "img_banners")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Stickers")
                put(ProductsTable.COLUMN_NAME, "Waterproof Vinyl Die-Cut Stickers")
                put(ProductsTable.COLUMN_DESCRIPTION, "Stunning custom stickers with strong adhesive, ideal for brand branding.")
                put(ProductsTable.COLUMN_MATERIAL, "Glossy Waterproof Vinyl")
                put(ProductsTable.COLUMN_SIZE, "Die-Cut 2\" x 2\"")
                put(ProductsTable.COLUMN_PRICE, 8.00)
                put(ProductsTable.COLUMN_IMAGE, "img_stickers")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Mugs")
                put(ProductsTable.COLUMN_NAME, "Custom Ceramic Mug Printing")
                put(ProductsTable.COLUMN_DESCRIPTION, "Excellent customized gift ceramic mugs. Microwave and dishwasher safe.")
                put(ProductsTable.COLUMN_MATERIAL, "Ceramic White 11oz")
                put(ProductsTable.COLUMN_SIZE, "Standard 11oz Mug")
                put(ProductsTable.COLUMN_PRICE, 450.00)
                put(ProductsTable.COLUMN_IMAGE, "img_mugs")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "T-Shirts")
                put(ProductsTable.COLUMN_NAME, "Custom Cotton Crewneck T-Shirts")
                put(ProductsTable.COLUMN_DESCRIPTION, "High-grade DTF printed personalized tees with vibrant lasting colors.")
                put(ProductsTable.COLUMN_MATERIAL, "100% Combed Cotton 180gsm")
                put(ProductsTable.COLUMN_SIZE, "L (Chest 40\")")
                put(ProductsTable.COLUMN_PRICE, 1150.00)
                put(ProductsTable.COLUMN_IMAGE, "img_tshirts")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "Wedding Cards")
                put(ProductsTable.COLUMN_NAME, "Elegant Linen Invitation Cards")
                put(ProductsTable.COLUMN_DESCRIPTION, "Beautifully textured invitation wedding cards with silver foil details.")
                put(ProductsTable.COLUMN_MATERIAL, "Textured Linen Card 300gsm")
                put(ProductsTable.COLUMN_SIZE, "5\" x 7\" Folded")
                put(ProductsTable.COLUMN_PRICE, 120.00)
                put(ProductsTable.COLUMN_IMAGE, "img_wedding_cards")
            },
            ContentValues().apply {
                put(ProductsTable.COLUMN_CATEGORY, "ID Cards")
                put(ProductsTable.COLUMN_NAME, "High-Quality PVC Corporate ID Cards")
                put(ProductsTable.COLUMN_DESCRIPTION, "Single/Double-sided plastic CR80 employee badge printing.")
                put(ProductsTable.COLUMN_MATERIAL, "PVC Plastic Matte")
                put(ProductsTable.COLUMN_SIZE, "CR80 standard badge")
                put(ProductsTable.COLUMN_PRICE, 140.00)
                put(ProductsTable.COLUMN_IMAGE, "img_id_cards")
            }
        )

        for (p in products) {
            db.insert(ProductsTable.TABLE_NAME, null, p)
        }
    }

    private fun seedOffers(db: SQLiteDatabase) {
        val offers = listOf(
            ContentValues().apply {
                put(OffersTable.COLUMN_TITLE, "Awurudu Printing Fiesta")
                put(OffersTable.COLUMN_DESCRIPTION, "Get huge discounts on business cards and banners this festive season.")
                put(OffersTable.COLUMN_DISCOUNT, "15% OFF")
            },
            ContentValues().apply {
                put(OffersTable.COLUMN_TITLE, "Christmas Special Savings")
                put(OffersTable.COLUMN_DESCRIPTION, "Enjoy Christmas savings on premium printing, custom gifts, and festive merchandise.")
                put(OffersTable.COLUMN_DISCOUNT, "20% OFF")
            },
            ContentValues().apply {
                put(OffersTable.COLUMN_TITLE, "Student Project Deal")
                put(OffersTable.COLUMN_DESCRIPTION, "Discounted printing for school and university students.")
                put(OffersTable.COLUMN_DISCOUNT, "25% OFF")
            }
        )

        for (o in offers) {
            db.insert(OffersTable.TABLE_NAME, null, o)
        }
    }

    private fun seedGallery(db: SQLiteDatabase) {
        val items = listOf(
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "Matte Finish Business Card")
                put(GalleryTable.COLUMN_IMAGE, "business_cards_sample")
            },
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "A5 Double Sided Flyer")
                put(GalleryTable.COLUMN_IMAGE, "flyers_sample")
            },
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "High Color Event Poster")
                put(GalleryTable.COLUMN_IMAGE, "poster_sample")
            },
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "Exhibition Flex Banner")
                put(GalleryTable.COLUMN_IMAGE, "img_banners_sample")
            },
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "Linen Textured Wedding Card")
                put(GalleryTable.COLUMN_IMAGE, "img_wedding_card")
            },
            ContentValues().apply {
                put(GalleryTable.COLUMN_TITLE, "Custom Glossy Ceramic Mug")
                put(GalleryTable.COLUMN_IMAGE, "img_printed_mug")
            }
        )

        for (item in items) {
            db.insert(GalleryTable.TABLE_NAME, null, item)
        }
    }

    private fun seedDemoUser(db: SQLiteDatabase) {
        val demoUser = ContentValues().apply {
            put(UsersTable.COLUMN_NAME, "Nimith Nimeshana")
            put(UsersTable.COLUMN_EMAIL, "nimith@test.com")
            put(UsersTable.COLUMN_PHONE, "0771234567")
            put(UsersTable.COLUMN_PASSWORD, HashUtils.sha256("Password123!"))
            put(UsersTable.COLUMN_ADDRESS, "No. 180, Dahaiyagama Junction, Anuradhapura, 50000")
        }
        db.insert(UsersTable.TABLE_NAME, null, demoUser)
    }
}
