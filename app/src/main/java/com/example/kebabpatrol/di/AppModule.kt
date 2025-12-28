package com.example.kebabpatrol.di

import com.example.kebabpatrol.data.remote.KebabApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Этот модуль живет, пока живет приложение
object AppModule {

    // ВСТАВЬ СЮДА СВОЮ ССЫЛКУ! СЛЭШ В КОНЦЕ ОБЯЗАТЕЛЕН!
    private const val BASE_URL = "https://69514b9770e1605a1089bd37.mockapi.io/api/v1/"

    @Provides
    @Singleton
    fun provideKebabApi(): KebabApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Чтоб понимал JSON
            .build()
            .create(KebabApi::class.java)
    }
    fun provideKebabRepository(api: KebabApi): com.example.kebabpatrol.domain.repository.KebabRepository {
        return com.example.kebabpatrol.data.repository.KebabRepositoryImpl(api)
    }
}