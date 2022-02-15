package com.ainrom.lbcremix.utils

import com.ainrom.lbcremix.model.Album

fun fakeAlbums(): List<Album> {
    val list = mutableListOf<Album>()
    for (i in 1L..10L) {
        list.add(
            Album(
            id = i,
            category = 2,
            title = "title $i",
            cover = "cover $i",
            thumbnail = "thumbnail $i"
        )
        )
    }
    return list
}