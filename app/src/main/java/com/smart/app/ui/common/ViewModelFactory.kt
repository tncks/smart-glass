package com.smart.app.ui.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smart.app.network.ApiClient
import com.smart.app.repository.category.CategoryRemoteDataSource
import com.smart.app.repository.category.CategoryRepository
import com.smart.app.repository.categorydetail.CategoryDetailRemoteDataSource
import com.smart.app.repository.categorydetail.CategoryDetailRepository
import com.smart.app.ui.category.CategoryViewModel
import com.smart.app.ui.categorydetail.CategoryDetailViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                val repository = CategoryRepository(CategoryRemoteDataSource(ApiClient.create()))
                @Suppress("UNCHECKED_CAST")
                CategoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CategoryDetailViewModel::class.java) -> {
                val repository = CategoryDetailRepository(CategoryDetailRemoteDataSource(ApiClient.create()))
                @Suppress("UNCHECKED_CAST")
                CategoryDetailViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel: ${modelClass.name}")
            }
        }
    }
}

// Refer, deprecated
/*
modelClass.isAssignableFrom(SampleViewModel::class.java) -> {
                val repository = CategoryRepository(CategoryRemoteDataSource(ApiClient.create()))
                @Suppress("UNCHECKED_CAST")
                SampleViewModel(repository) as T
            }
 */
//            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
//                val repository = HomeRepository()
//                @Suppress("UNCHECKED_CAST")
//                HomeViewModel(repository) as T
//            }