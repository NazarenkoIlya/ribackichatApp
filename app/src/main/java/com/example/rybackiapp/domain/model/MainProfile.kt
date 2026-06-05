package com.example.rybackiapp.domain.model

data class MainProfile(
    val id: String = "",
    val name: String? = null,
    val email: String? = null,
    val year: String? = null,
    val tagName: String? = null,
    val interests: List<Int>? = null,
    val mainPhotoUrl: String? = null,
    val photos: Map<String, String>? = null
)

data class UserProfile(
    val id: String = "",
    val name: String? = null,
    val year: String? = null,
    val tagName: String? = null,
    val interests: List<Int>? = null,
    val mainPhotoUrl: String? = null,
    val chatId: String = ""
)

data class UserProfileList(
    val profiles: List<UserProfile> = emptyList()
)

data class UserPreview(
    val uid: String = "",
    val name: String = "",
    val mainPhotoUrl: String? = null,
)