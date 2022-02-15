package com.ainrom.lbcremix.utils

import com.ainrom.lbcremix.model.Album

object FakeData {
    var albums1 = {
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
        list
    }

    var albums2 = {
        val list = mutableListOf<Album>()
        for (i in 1L..5L) {
            list.add(
                Album(
                    id = i,
                    category = 3,
                    title = "title $i",
                    cover = "cover $i",
                    thumbnail = "thumbnail $i"
                )
            )
        }
        list
    }

    val album = Album(
        id = 999,
        category = 5,
        title = "title XXX",
        cover = "cover XXX",
        thumbnail = "thumbnail XXX"
    )
}
