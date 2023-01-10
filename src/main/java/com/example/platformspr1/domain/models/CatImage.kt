package com.example.platformspr1.domain.models

//import androidx.room.Entity

//@Entity(tableName = "cat_images")
data class CatImage (
    var id: String,
    var url: String,
    var width: Int,
    var height: Int
)