package com.smart.app.model

import com.google.gson.annotations.SerializedName

data class CategoryDetail(
    @SerializedName("top_selling") val topSelling: TopSelling
)

data class TopSelling(
    val title: Title,
    val categories: List<Category>
)


// Refer
/*
data class CategoryDetail(
    @SerializedName("top_selling") val topSelling: TopSelling,
    val promotions: Promotion
)

data class TopSelling(
    val title: Title,
    val categories: List<Category>
)

data class Promotion(
    val title: Title,
    val items: List<Product>
)
 */