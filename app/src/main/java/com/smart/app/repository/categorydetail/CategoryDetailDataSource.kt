package com.smart.app.repository.categorydetail

import com.smart.app.model.CategoryDetail

interface CategoryDetailDataSource {

    suspend fun getCategoryDetail(): CategoryDetail
}