package com.example.utils

object Constants {
    const val STATUS_PENDING = "Pending"
    const val STATUS_PROCESSING = "Processing"
    const val STATUS_PRINTING = "Printing"
    const val STATUS_READY = "Ready"
    const val STATUS_COMPLETED = "Completed"
    const val STATUS_CANCELLED = "Cancelled"

    const val DELIVERY_PICKUP = "Pickup"
    const val DELIVERY_COURIER = "Delivery"

    val CATEGORIES = listOf(
        "Business Cards",
        "Flyers",
        "Posters",
        "Banners",
        "Stickers",
        "Mugs",
        "T-Shirts",
        "Wedding Cards",
        "ID Cards"
    )
}
