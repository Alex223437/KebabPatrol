package com.example.kebabpatrol.data.remote

import com.example.kebabpatrol.data.remote.dto.KebabDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface KebabApi {
    @GET("places")
    suspend fun getKebabs(): List<KebabDto>

    @POST("places")
    suspend fun addKebab(@Body kebab: KebabDto)
}