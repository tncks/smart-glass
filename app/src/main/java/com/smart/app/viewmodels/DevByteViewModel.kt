package com.smart.app.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.smart.app.database.getDatabase
import com.smart.app.repository.VideosRepository
import kotlinx.coroutines.launch
import java.io.IOException


class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val videosRepository = VideosRepository(getDatabase(application))


    val playlist = videosRepository.videos


    private var _eventNetworkError = MutableLiveData(false)


    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData(false)


    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    init {
        refreshDataFromRepository()
    }


    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                videosRepository.refreshVideos()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                networkError.printStackTrace()
                if (playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                if (playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


    class Factory(private val cont: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(DevByteViewModel::class.java) -> {
                    @Suppress("UNCHECKED_CAST")
                    DevByteViewModel(cont) as T
                }
                else -> {
                    throw IllegalArgumentException("Fail unable to construct viewmodel: ${modelClass.name}")
                }
            }

        }
    }
}