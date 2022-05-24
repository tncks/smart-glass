package com.smart.app.repository.category

import com.smart.app.model.Category
import com.smart.app.network.ApiClient

class CategoryRemoteDataSource(private val apiClient: ApiClient) : CategoryDataSource {
    override suspend fun getCategories(uid: String): List<Category>? {
        return apiClient.getCategories(uid)
    }

}