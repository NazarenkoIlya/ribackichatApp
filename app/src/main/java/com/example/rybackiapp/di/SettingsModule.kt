package com.example.rybackiapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.rybackiapp.data.repository.FilterRepositoryImpl
import com.example.rybackiapp.data.repository.SettingsRepositoryImpl
import com.example.rybackiapp.data.storage.SettingStorage
import com.example.rybackiapp.data.storage.SettingStorageImpl
import com.example.rybackiapp.domain.repository.FilterRepository
import com.example.rybackiapp.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    fun provideSettingStorage(
        @SettingsDataStore dataStore: DataStore<Preferences>
    ): SettingStorage {
        return SettingStorageImpl(dataStore)
    }

    @Provides
    fun provideSettingRepository(
        settingStorage: SettingStorage
    ): SettingsRepository {
        return SettingsRepositoryImpl(settingStorage)
    }
}