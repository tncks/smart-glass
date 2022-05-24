package com.smart.app.network

import com.smart.app.common.MAIL_FORWARDING_SERVER_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface TESTInterface {
    @POST("/")
    suspend fun forwardEmail(
        @Body requestBody: RequestBody
    ): Response<ResponseBody>


    companion object {

        fun create(): TESTInterface {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()


            return Retrofit.Builder()
                .baseUrl(MAIL_FORWARDING_SERVER_BASE_URL)
                .client(client)
                .build()
                .create(TESTInterface::class.java)

        }

    }


}

