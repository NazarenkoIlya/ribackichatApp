package com.example.rybackiapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ConnectedRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalReference



@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }


    @Provides
    @Singleton
    @ConnectedRef
    fun provideConnectedRef(database: FirebaseDatabase): DatabaseReference{
        return database.getReference(".info/connected")
    }

    @Provides
    @Singleton
    @GlobalReference
    fun provideDatabaseReference(
        database: FirebaseDatabase
    ): DatabaseReference {
        return database.reference
    }
}