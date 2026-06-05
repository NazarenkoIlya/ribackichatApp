package com.example.rybackiapp.di


import com.example.rybackiapp.data.repository.ImageStorageRepositoryImpl
import com.example.rybackiapp.domain.repository.ImageStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
//import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ImageStorageModule {

    @Provides
    @Singleton
    fun provideImageStorageRepository(
        supabaseClient: SupabaseClient
    ): ImageStorageRepository {
        return  ImageStorageRepositoryImpl(supabaseClient)//(supabaseClient)
    }

}