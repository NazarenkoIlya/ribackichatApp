package com.example.rybackiapp.di

import android.content.Context
import com.example.rybackiapp.data.repository.InterestRepositoryImpl
import com.example.rybackiapp.domain.repository.InterestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object InterestsModule {

    @Provides
    fun provideInterestRepository(
        @ApplicationContext context: Context
    ): InterestRepository {
        return InterestRepositoryImpl(context.assets)
    }
}