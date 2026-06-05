package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ProfileRepository
import javax.inject.Inject


class EditMainImageUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(uid: String, url: String) {
        profileRepository.uploadMainImage(uid, url)
    }
}