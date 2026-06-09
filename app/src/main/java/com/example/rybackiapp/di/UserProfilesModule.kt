package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.ProfileRepositoryImpl
import com.example.rybackiapp.data.repository.UsersProfileRepositoryImpl
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import com.example.rybackiapp.domain.usecase.CreateUserProfileUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfilesListUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfilesUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfilesModule {

    @Provides
    @Singleton
    fun provideUsersProfileRepository(
        @GlobalReference database: DatabaseReference
    ): UsersProfileRepository {
        return UsersProfileRepositoryImpl(database)
    }

    @Provides
    fun provideGetUserProfilesListUseCase(
        repository: UsersProfileRepository
    ): GetUserProfilesListUseCase {
        return GetUserProfilesListUseCase(repository)
    }

    @Provides
    fun provideGetUserProfilesUseCase(
        repository: UsersProfileRepository
    ): GetUserProfilesUseCase {
        return GetUserProfilesUseCase(repository)
    }

}