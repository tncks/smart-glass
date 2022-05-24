package com.smart.app.repository.auth

import com.smart.app.network.RESTInterface
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRemoteDataSource(private val signClient: RESTInterface) : AuthDataSource {
    override suspend fun checkUserLogin(
        requestBody: RequestBody
    ): Response<ResponseBody> {
        return signClient.checkUserLogin(requestBody)
    }

}
