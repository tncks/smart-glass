package com.smart.app.repository.auth

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthDataSource {
    suspend fun checkUserLogin(
        requestBody: RequestBody
    ): Response<ResponseBody>
}