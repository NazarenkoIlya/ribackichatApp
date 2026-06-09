package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.ChatRepositoryImpl
import com.example.rybackiapp.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatModule {


    @Provides
    @Singleton

    fun provideChatRepository(
        firebaseAuth: FirebaseAuth,
        @GlobalReference database: DatabaseReference
    ): ChatRepository {
        return ChatRepositoryImpl(
            auth = firebaseAuth,
            databaseRef = database
        )
    }
}