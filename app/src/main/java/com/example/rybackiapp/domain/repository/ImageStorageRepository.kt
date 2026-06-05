package com.example.rybackiapp.domain.repository

import java.io.File

interface ImageStorageRepository {
    suspend fun getPublicUrlForImage(imagePath: String): String
    suspend fun uploadImage(fileName: String, bytes: ByteArray): String
}