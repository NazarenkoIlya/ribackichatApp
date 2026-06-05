package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ImageStorageRepository
import javax.inject.Inject

class GetPublicImageUrlUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository
) {
    suspend operator fun invoke(imagePath: String): String {
        return imageStorageRepository.getPublicUrlForImage(imagePath)
    }

}