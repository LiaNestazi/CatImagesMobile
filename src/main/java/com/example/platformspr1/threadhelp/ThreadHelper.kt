package com.example.platformspr1.threadhelp

import com.example.platformspr1.domain.models.CatImage
import com.example.platformspr1.domain.usecase.GetCatImagesUseCase
import kotlinx.coroutines.Runnable

class ThreadHelper: Runnable  {
    var imagesList: ArrayList<CatImage> = ArrayList()
    val get: GetCatImagesUseCase = GetCatImagesUseCase()
    override fun run() {
        imagesList = get.execute()
    }
    fun getValue(): ArrayList<CatImage>{
        return imagesList
    }
}