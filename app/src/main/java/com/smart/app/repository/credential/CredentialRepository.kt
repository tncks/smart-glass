package com.smart.app.repository.credential

import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody

class CredentialRepository(
    private val remoteDataSource: CredentialRemoteDataSource
) {

    suspend fun forwardEmail(requestBody: RequestBody): Response<ResponseBody> {
        return remoteDataSource.forwardEmail(requestBody)
    }
}