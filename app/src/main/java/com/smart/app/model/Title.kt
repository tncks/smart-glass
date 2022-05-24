package com.smart.app.model

import com.google.gson.annotations.SerializedName

data class Title(
    val text: String,
    @SerializedName("icon_url") val iconUrl: String?
)


// Refer
/*

data class Product(
    @SerializedName("brand_name") val brandName: String?,
    val label: String,
    @SerializedName("discount_rate") val discountRate: Int,
    val price: Int,
    @SerializedName("thumbnail_image_url") val thumbnailImageUrl: String?,
    @SerializedName("representative_image_url") val representativeImageUrl: String?,
    @SerializedName("product_id") val productId: String
)

 */