package com.example.rybackiapp.domain.model

data class InterestItem(
    val id: Int,
    val name: String,
    val nameEng: String
)

data class InterestGroup(
    val id: Int,
    val name: String,
    val nameEng: String,
    val items: List<InterestItem>
)