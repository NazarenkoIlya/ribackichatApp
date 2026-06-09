package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.ProfileRepositoryImpl
import com.example.rybackiapp.data.repository.UserIdCacheRepositoryImpl
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import com.example.rybackiapp.domain.usecase.CreateUserProfileUseCase
import com.example.rybackiapp.domain.usecase.EditProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton

    fun provideProfileRepository(
        firebaseAuth: FirebaseAuth,
        @GlobalReference     database: DatabaseReference
    ): ProfileRepository {
        return ProfileRepositoryImpl(firebaseAuth, database)
    }


    @Provides
    @Singleton
    fun provideUserIdCacheRepository(
          repository: ProfileRepository
    ): UserIdCacheRepository {
        return UserIdCacheRepositoryImpl(repository)
    }

    @Provides
    fun provideCreateUserProfileUseCase(
          repository: ProfileRepository
    ): CreateUserProfileUseCase {
        return CreateUserProfileUseCase(repository)
    }

    fun provideEditProfileUseCase(
            repository: ProfileRepository
    ): EditProfileUseCase {
        return EditProfileUseCase(repository)
    }

}