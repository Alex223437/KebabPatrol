package com.example.kebabpatrol.data.remote

import com.example.kebabpatrol.data.remote.dto.KebabDto
import retrofit2.http.GET

interface KebabApi {
    // Мы говорим: "Сходи по адресу BASE_URL + places и принеси список"
    @GET("places")
    suspend fun getKebabs(): List<KebabDto>
}