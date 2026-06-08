package com.example.rybackiapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FilterDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStore

private const val FILTER_PREFERENCES = "filer_prefs"
private const val SETTINGS_PREFERENCES = "settings_prefs"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @FilterDataStore
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(FILTER_PREFERENCES) }
        )
    }

    @SettingsDataStore
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(SETTINGS_PREFERENCES) }
        )
    }
}