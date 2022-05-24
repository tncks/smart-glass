package com.smart.app.network


import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @PUT("users/{uid}/account.json")
    suspend fun pushOneAccount(
        @Path("uid") uid: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

    @PUT("users/{uid}/categories/{number}.json")
    suspend fun updateCategories(
        @Path("uid") uid: String,
        @Path("number") number: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

    @PATCH("users/{uid}/categories/{number}.json")
    suspend fun updateItemProfileStyle(
        @Path("uid") uid: String,
        @Path("number") number: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>


    @PATCH("users/{uid}/account.json")
    suspend fun updateUserResetPw(
        @Path("uid") uid: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

}