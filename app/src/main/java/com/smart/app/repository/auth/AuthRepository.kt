package com.smart.app.repository.auth

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource
) {

    suspend fun checkUserLogin(requestBody: RequestBody): Response<ResponseBody> {
        return remoteDataSource.checkUserLogin(requestBody)
    }
}