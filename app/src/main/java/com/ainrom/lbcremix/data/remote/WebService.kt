package com.ainrom.lbcremix.data.remote

import com.ainrom.lbcremix.model.Album
import retrofit2.http.*

interface WebService {

    @GET("img/shared/technical-test.json")
    suspend fun fetchAlbums(): List<Album>
}
