package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.NotificationRepositoryImpl
import com.example.rybackiapp.data.repository.ProfileRepositoryImpl
import com.example.rybackiapp.data.repository.PushNotificationRepositoryImpl
import com.example.rybackiapp.data.service.NotificationApi
import com.example.rybackiapp.domain.repository.NotificationRepository
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.PushNotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton

    fun provideNotificationRepository(
        @GlobalReference database: DatabaseReference
    ): NotificationRepository {
        return NotificationRepositoryImpl(
            database = database
        )
    }

    @Provides
    @Singleton
    fun providePushNotificationRepository(
        api: NotificationApi
    ): PushNotificationRepository {
        return PushNotificationRepositoryImpl(
            api = api
        )
    }

}