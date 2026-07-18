package com.example.database

import android.provider.BaseColumns

object DatabaseContract {

    object UsersTable : BaseColumns {
        const val TABLE_NAME = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ADDRESS = "address"
    }

    object ProductsTable : BaseColumns {
        const val TABLE_NAME = "products"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_MATERIAL = "material"
        const val COLUMN_SIZE = "size"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
    }

    object OrdersTable : BaseColumns {
        const val TABLE_NAME = "orders"
        const val COLUMN_ORDER_ID = "order_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PAPER_TYPE = "paper_type"
        const val COLUMN_CUSTOM_TEXT = "custom_text"
        const val COLUMN_DESIGN_PATH = "design_path"
        const val COLUMN_DELIVERY_METHOD = "delivery_method"
        const val COLUMN_STATUS = "status"
        const val COLUMN_DATE = "date"
        const val COLUMN_TOTAL_PRICE = "total_price"
    }

    object OffersTable : BaseColumns {
        const val TABLE_NAME = "offers"
        const val COLUMN_OFFER_ID = "offer_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DISCOUNT = "discount"
    }

    object GalleryTable : BaseColumns {
        const val TABLE_NAME = "gallery"
        const val COLUMN_GALLERY_ID = "gallery_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_IMAGE = "image"
    }

    object SavedDesignsTable : BaseColumns {
        const val TABLE_NAME = "saved_designs"
        const val COLUMN_DESIGN_ID = "design_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_FILE_PATH = "file_path"
    }

    object AddressesTable : BaseColumns {
        const val TABLE_NAME = "addresses"
        const val COLUMN_ADDRESS_ID = "address_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_FULL_ADDRESS = "full_address"
        const val COLUMN_CITY = "city"
        const val COLUMN_POSTAL_CODE = "postal_code"
    }
}
