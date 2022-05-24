package com.smart.app.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_id") val categoryId: String,
    val label: String,
    @SerializedName("thumbnail_image_url") val thumbnailImageUrl: String,
    val updated: Boolean,
    val location: String,
    val period: String,
    val memo: String
)


// Refer
//data class Banner(
//    @SerializedName("background_image_url") val backgroundImageUrl: String,
//    val badge: BannerBadge,
//    val label: String,
//    @SerializedName("product_detail") val productDetail: Product
//)


//data class BannerBadge(
//    val label: String,
//    @SerializedName("background_color") val backgroundColor: String
//)
