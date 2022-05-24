package com.smart.app.network

import com.smart.app.common.BACK_AZURE_ACCOUNT_LOGIN_AUTH_RESTAPI_BASEURL
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface RESTInterface {
    @POST("/api")
    suspend fun checkUserLogin(
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

    @POST("/del")
    suspend fun deleteCategories(
        @Body requestBody: RequestBody
    ): Response<ResponseBody>


    companion object {

        fun create(): RESTInterface {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()


            return Retrofit.Builder()
                .baseUrl(BACK_AZURE_ACCOUNT_LOGIN_AUTH_RESTAPI_BASEURL)
                .client(client)
                .build()
                .create(RESTInterface::class.java)

        }

    }


}

