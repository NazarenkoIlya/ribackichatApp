package com.example.rybackiapp.domain.usecase

import android.util.Log
import com.example.rybackiapp.domain.repository.ImageStorageRepository
import com.example.rybackiapp.domain.repository.ProfileRepository
import java.io.File
import javax.inject.Inject

class UploadUserAvatarUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository,
) {
    suspend operator fun invoke(fileName: String, uid: String, bytes: ByteArray): Result<String> {

        return try {
            val path = "avatars/$uid/$fileName"
            val url = imageStorageRepository.uploadImage(path, bytes)
            Log.d("COMPRESS", "**invoke: ${url}")
            //profileRepository.uploadMainImage(uid, url)
            Log.d("COMPRESS", "***invoke: ${url}")
            Result.success((url))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}