package com.example.platformspr1.domain.usecase

import com.example.platformspr1.domain.models.CatImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class GetCatImagesUseCase {

    fun execute(): ArrayList<CatImage>{
        val client = OkHttpClient()


        var key = "live_3w8LZsV69weUq6v ZEsbuE9Fwo07HmvXEIOM ErQ282iSgEYUL6buyyWr 3hfiw66LU"
        val request = Request.Builder()
            .url("https://api.thecatapi.com/v1/images/search?limit=20")
            .header("Authorization", key)
            .get()
            .build()


        val response = client.newCall(request).execute()
        val jsonDataString = response.body?.string()

        val json = JSONArray(jsonDataString)

        var catList : ArrayList<CatImage> = ArrayList()
        for (i in 0 until json.length()){
            val item = json.getJSONObject(i)
            var img: CatImage = CatImage(item.getString("id"),
            item.getString("url"),
            item.getInt("width"),
            item.getInt("height"))
            catList.add(img)
        }
        return catList
    }
}