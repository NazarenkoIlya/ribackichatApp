package com.example.rybackiapp.di

import android.content.Context
import com.example.rybackiapp.data.model.room.dao.MessageDao
import com.example.rybackiapp.data.repository.DataBaseMessageRepositoryImpl
import com.example.rybackiapp.data.repository.InterestRepositoryImpl
import com.example.rybackiapp.domain.repository.DataBaseMessageRepository
import com.example.rybackiapp.domain.repository.InterestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MessageDraftModule {

    @Provides
    fun provideDataBaseMessageRepository(
        messageDao: MessageDao
    ): DataBaseMessageRepository {
        return DataBaseMessageRepositoryImpl(messageDao)
    }
}