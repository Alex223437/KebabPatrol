package com.example.kebabpatrol.di

import android.app.Application
import com.example.kebabpatrol.data.remote.KebabApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.room.Room
import com.example.kebabpatrol.data.local.KebabDao
import com.example.kebabpatrol.data.local.KebabDatabase
import com.example.kebabpatrol.data.repository.KebabRepositoryImpl
import com.example.kebabpatrol.domain.repository.KebabRepository

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
//    fun provideKebabRepository(api: KebabApi): com.example.kebabpatrol.domain.repository.KebabRepository {
//        return com.example.kebabpatrol.data.repository.KebabRepositoryImpl(api)
//    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): KebabDatabase {
        return Room.databaseBuilder(
            app,
            KebabDatabase::class.java,
            "kebab_db.db"
        ).build()
    }

    // 2. ВЫДАЕМ DAO (чтобы Репозиторий не лез в саму базу)
    @Provides
    @Singleton
    fun provideDao(db: KebabDatabase): KebabDao {
        return db.dao // Исправлено: db.dao, а не db.kebabDao
    }

//    @Provides
//    @Singleton
//    fun provideKebabRepository(
//        api: KebabApi,
//        dao: KebabDao // <--- 1. ДОБАВЬ ЭТОТ АРГУМЕНТ СЮДА!
//    ): KebabRepository {
//        // 2. И ПЕРЕДАЙ ЕГО В КОНСТРУКТОР!
//        return KebabRepositoryImpl(api, dao)
//    }
}