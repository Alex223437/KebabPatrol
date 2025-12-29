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
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://69514b9770e1605a1089bd37.mockapi.io/api/v1/"

    @Provides
    @Singleton
    fun provideKebabApi(): KebabApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KebabApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): KebabDatabase {
        return Room.databaseBuilder(
            app,
            KebabDatabase::class.java,
            "kebab_db.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: KebabDatabase): KebabDao {
        return db.dao
    }
}