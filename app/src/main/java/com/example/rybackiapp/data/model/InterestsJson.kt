package com.example.rybackiapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class InterestData(
    val id: Int,
    val name: String,
    @SerialName("name_en")
    val nameEng: String
)

@Serializable
data class InterestGroupData(
    val id: Int,
    val name: String,
    @SerialName("name_en")
    val nameEng: String,
    val items: List<InterestData>
)

@Serializable
data class InterestsJson(
    val groups: List<InterestGroupData>
)