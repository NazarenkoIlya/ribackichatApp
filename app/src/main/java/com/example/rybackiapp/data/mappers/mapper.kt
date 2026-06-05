package com.example.rybackiapp.data.mappers

//import com.example.rybackiapp.data.model.UserDto
import androidx.compose.ui.res.integerResource
import com.example.rybackiapp.data.model.InterestData
import com.example.rybackiapp.data.model.InterestGroupData
import com.example.rybackiapp.data.model.room.dbo.MessagesDbo
import com.example.rybackiapp.domain.model.Chat
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.model.MessageDraft
import com.example.rybackiapp.domain.model.PrivateChat

//fun UserDto.toDomain(): Profile {
//    return Profile(
//        id = uid,
//        name = displayName,
//        email = email,
//        year = age,
//        mainPhotoUrl = photoUrl,
//    )
//}

fun Chat.toMap(): Map<String, Any?> {
    return when (this) {
        is PrivateChat -> mapOf(
            "participants" to this.participants.associateWith { true },
            "lastMessage" to lastMessage,
            "lastTimestamp" to lastTimestamp
        )
    }
}

fun InterestData.toMap(): InterestItem {
    return InterestItem(
        id = id,
        name = name,
        nameEng = nameEng
    )
}

fun InterestGroupData.toMap(): InterestGroup {
    return InterestGroup(
        id = id,
        name = name,
        nameEng = nameEng,
        items = items.map { it.toMap() }
    )
}

fun MessageDraft.toMapMessagesDbo(): MessagesDbo {
    return MessagesDbo(
        chatId = chatId,
        text = text
    )
}

fun MessagesDbo.toMapMessageDraft(): MessageDraft {
    return MessageDraft(
        chatId = chatId,
        text = text
    )
}
