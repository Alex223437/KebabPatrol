package com.example.kebabpatrol.di

import com.example.kebabpatrol.domain.repository.KebabRepository
import com.example.kebabpatrol.data.repository.KebabRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindKebabRepository(
        kebabRepositoryImpl: KebabRepositoryImpl
    ): KebabRepository
}
