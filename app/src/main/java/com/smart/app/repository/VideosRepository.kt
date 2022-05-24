package com.smart.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.smart.app.database.VideosDatabase
import com.smart.app.database.asDomainModel
import com.smart.app.domain.DevByteVideo
import com.smart.app.network.DevByteNetwork
import com.smart.app.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }


    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {

            val playlist = DevByteNetwork.devbytes.getPlaylist()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }

}