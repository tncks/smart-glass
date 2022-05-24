package com.smart.app.repository.category

import com.smart.app.model.Category

interface CategoryDataSource {
    suspend fun getCategories(uid: String): List<Category>?
}