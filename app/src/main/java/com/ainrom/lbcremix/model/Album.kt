package com.ainrom.lbcremix.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
class Album constructor(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("albumId")
    var category: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("url")
    var cover: String,

    @SerializedName("thumbnailUrl")
    var thumbnail: String
)