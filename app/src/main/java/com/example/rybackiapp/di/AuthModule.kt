package com.example.rybackiapp.di

import com.example.rybackiapp.data.repository.AuthRepositoryImpl
import com.example.rybackiapp.domain.repository.AuthRepository
import com.example.rybackiapp.domain.usecase.CheckAuthStateUseCase
import com.example.rybackiapp.domain.usecase.SignInUseCase
import com.example.rybackiapp.domain.usecase.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton

    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseMessaging: FirebaseMessaging,
        @GlobalReference database: DatabaseReference
    ): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth,
            firebaseMessaging = firebaseMessaging,
            database = database
        )
    }

    @Provides
    fun provideSignInUseCase(
        repository: AuthRepository
    ): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    fun provideSignUpUseCase(
        repository: AuthRepository
    ): SignUpUseCase {
        return SignUpUseCase(repository)
    }

    @Provides
    fun provideCheckAuthStateUseCase(
        repository: AuthRepository
    ): CheckAuthStateUseCase {
        return CheckAuthStateUseCase(repository)
    }
}