package com.smart.app.repository.categorydetail

import com.smart.app.model.CategoryDetail
import com.smart.app.network.ApiClient

class CategoryDetailRemoteDataSource(private val apiClient: ApiClient) : CategoryDetailDataSource {
    override suspend fun getCategoryDetail(): CategoryDetail {
        return apiClient.getCategoryDetail()
    }

}