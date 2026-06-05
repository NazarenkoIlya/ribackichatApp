package com.example.rybackiapp.di

import android.content.Context
import androidx.room.Room
import com.example.rybackiapp.data.model.room.MessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MessagesDatabase::class.java,
        "starwars.db"
    ).fallbackToDestructiveMigration(false)
        .build()

    @Provides
    @Singleton
    fun provideMessageDao(db: MessagesDatabase) = db.messageDao()
}