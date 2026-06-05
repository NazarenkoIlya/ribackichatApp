package com.example.rybackiapp.data.repository

import android.service.media.MediaBrowserService
import android.util.Log
import kotlin.Result
import com.example.rybackiapp.domain.repository.ImageStorageRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
//import io.github.jan.supabase.SupabaseClient
//import io.github.jan.supabase.storage.storage
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ImageStorageRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ImageStorageRepository {

    //private val storageBucket = supabaseClient.storage["images"]
    override suspend fun getPublicUrlForImage(imagePath: String): String {
        return ""//storageBucket.publicUrl(imagePath)
    }

    override suspend fun uploadImage(
        fileName: String,
        bytes: ByteArray
    ): String {
        val bucket = supabaseClient.storage.from("images")
        bucket.upload(
            path = fileName,
            data = bytes
        )
        val url = bucket.publicUrl(fileName)
        return url
    }
}