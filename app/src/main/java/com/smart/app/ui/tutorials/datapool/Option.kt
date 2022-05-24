package com.smart.app.ui.tutorials.datapool

import com.google.gson.annotations.SerializedName

data class Option(
    @SerializedName("test_one") var testOne: String? = null,
    @SerializedName("test_two") var testTwo: String? = null
)