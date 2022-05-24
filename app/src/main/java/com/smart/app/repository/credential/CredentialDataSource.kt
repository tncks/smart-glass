package com.smart.app.repository.credential

import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface CredentialDataSource {
    suspend fun forwardEmail(requestBody: RequestBody): Response<ResponseBody>
}