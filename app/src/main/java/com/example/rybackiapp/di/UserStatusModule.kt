package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.UserStatusRepositoryImpl
import com.example.rybackiapp.data.repository.UsersProfileRepositoryImpl
import com.example.rybackiapp.domain.repository.UserStatusRepository
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserStatusModule {
    @Provides
    @Singleton
    fun provideUserStatusRepository(
        @GlobalReference database: DatabaseReference,
        @ConnectedRef connectedRef: DatabaseReference
    ): UserStatusRepository {
        return UserStatusRepositoryImpl(
            databaseRef = database,
            connectedRef = connectedRef
        )
    }
}