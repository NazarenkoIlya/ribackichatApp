package com.example.rybackiapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.rybackiapp.data.repository.FilterRepositoryImpl
import com.example.rybackiapp.domain.repository.FilterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object FilterModule {

    @Provides
    fun provideFilterRepository(
        @FilterDataStore dataStore: DataStore<Preferences>
    ): FilterRepository {
        return FilterRepositoryImpl(dataStore)
    }
}