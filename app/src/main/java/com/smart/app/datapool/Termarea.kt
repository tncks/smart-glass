package com.smart.app.datapool

import com.google.gson.annotations.SerializedName

data class Termarea(
    @SerializedName("test_one") var testOne: String? = null,
    @SerializedName("test_two") var testTwo: String? = null
)
