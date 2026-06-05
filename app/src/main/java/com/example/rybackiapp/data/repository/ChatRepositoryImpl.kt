package com.example.rybackiapp.data.repository

import android.util.Log
import androidx.lifecycle.compose.LifecycleStartEffect
import com.example.rybackiapp.data.mappers.toMap
import com.example.rybackiapp.domain.model.Chat
import com.example.rybackiapp.domain.model.Message
import com.example.rybackiapp.domain.model.PrivateChat
import com.example.rybackiapp.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val databaseRef: DatabaseReference
) : ChatRepository {

    //region Первая версия создания чата
    suspend fun createChat(
        chatId: String,
        title: String?,
        participants: List<String>,
        isGroup: Boolean,
    ): String {


        val chatData = mapOf(
            "title" to (title ?: ""),
            "isGroup" to isGroup,
            "participants" to participants.associateWith { true },
            "lastMessage" to "",
            "lastTimestamp" to System.currentTimeMillis()
        )

        databaseRef.child("chats")
            .child(chatId)
            .setValue(chatData)
            .await()

        participants.forEach { uid ->
            databaseRef.child("userChats")
                .child(uid)
                .child(chatId)
                .setValue(true)
                .await()
        }

        participants.forEach { uid ->
            databaseRef.child("chatMembers")
                .child(chatId)
                .child(uid)
                .setValue(mapOf("unreadCount" to 0))
                .await()
        }

        participants.forEach { uid ->
            databaseRef.child("userUnreadCounts")
                .child(uid)
                .setValue(mapOf("totalUnread" to 0))
                .await()
        }
        return chatId
    }
    //endregion

    override suspend fun createPrivateChat(
        chatId: String,
        participants: List<String>,
        lastMessage: String,
        lastTimestamp: Long
    ): String {

        val chatData = mapOf(
            PARTICIPANTS to participants.associateWith { true },
            LAST_MESSAGE to lastMessage,
            LAST_TIMESTAMP to lastTimestamp
        )

        databaseRef.child(CHATS)
            .child(chatId)
            .setValue(chatData)
            .await()

        participants.forEach { uid ->
            databaseRef.child(USER_CHATS)
                .child(uid)
                .child(chatId)
                .setValue(true)
                .await()
        }

        participants.forEach { uid ->
            databaseRef.child(CHAT_MEMBERS)
                .child(chatId)
                .child(uid)
                .setValue(mapOf(UNREAD_COUNT to 0))
                .await()
        }

        return chatId
    }


    override suspend fun isChatCreated(chatId: String): Boolean {
        val snapshot = databaseRef.child(CHATS).get().await()
        val chatIds = snapshot.children.mapNotNull { it.key }
        return chatIds.contains(chatId)
    }

    override suspend fun sendMessage(chatId: String, message: Message) {

        val messageId = databaseRef.child(MESSAGES)
            .child(chatId)
            .push()
            .key ?: throw Exception("No messageId")

        val messageData = mapOf(
            SENDER_ID to message.senderId,
            TEXT to message.text,
            TIMESTAMP to message.timestamp
        )


        val updates = mutableMapOf<String, Any?>()

        updates["$MESSAGES/$chatId/$messageId"] = messageData


//        updateChatPreview(
//            chatId = chatId,
//            text = message.text,
//            timestamp = message.timestamp
//        )
        updates["$CHATS/${chatId}/$LAST_MESSAGE"] = message.text
        updates["$CHATS/${chatId}/$LAST_SENDER_ID"] = message.senderId
        updates["$CHATS/${chatId}/$LAST_TIMESTAMP"] = message.timestamp


        val participants = databaseRef
            .child(CHATS)
            .child(chatId)
            .child(PARTICIPANTS)
            .get()
            .await()
            .children.mapNotNull { it.key }


        participants.forEach { uid ->
            if (uid != message.senderId) {
                updates["$CHAT_MEMBERS/$chatId/$uid/$UNREAD_COUNT"] =
                    ServerValue.increment(1)
            }
        }

        databaseRef.updateChildren(updates).await()
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {

        val messageRef = databaseRef.child("$MESSAGES/$chatId/$messageId")
        messageRef.removeValue().addOnSuccessListener {

            val newLastQuery = databaseRef
                .child("$MESSAGES/$chatId")
                .orderByChild(TIMESTAMP)
                .limitToLast(1)

            newLastQuery.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    snapshot.children.forEach { it ->
                        val text = it.child(TEXT).getValue(String::class.java)
                            ?: return@addOnSuccessListener
                        val senderId = it.child(SENDER_ID).getValue(String::class.java)
                            ?: return@addOnSuccessListener
                        val timestamp = it.child(TIMESTAMP).getValue(Long::class.java)
                            ?: return@addOnSuccessListener
                        updateChatPreview(chatId, senderId, text, timestamp)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteChat(chatId)
                    }
                }
            }
        }
    }

    private suspend fun deleteChat(chatId: String){
        val chatSnapshot = databaseRef.child(CHATS).child(chatId).get().await()
        val uids = chatSnapshot.child(PARTICIPANTS).children.mapNotNull { it.key }
        val updates = mutableMapOf<String, Any?>()

        updates["$CHATS/$chatId"] = null
        updates["$CHAT_MEMBERS/$chatId"] = null
        updates["$MESSAGES/$chatId"] = null

        uids.forEach { uid ->
            updates["$USER_CHATS/$uid/$chatId"] = null
        }

        databaseRef.updateChildren(updates).await()
    }

    override suspend fun deletePrivateChat(chatId: String) {
        deleteChat(chatId )
//        val chatSnapshot = databaseRef.child(CHATS).child(chatId).get().await()
//        val uids = chatSnapshot.child(PARTICIPANTS).children.mapNotNull { it.key }
//        val updates = mutableMapOf<String, Any?>()
//
//        updates["$CHATS/$chatId"] = null
//        updates["$CHAT_MEMBERS/$chatId"] = null
//        updates["$MESSAGES/$chatId"] = null
//
//        uids.forEach { uid ->
//            updates["$USER_CHATS/$uid/$chatId"] = null
//        }
//
//        databaseRef.updateChildren(updates).await()
    }

    private fun updateChatPreview(chatId: String, senderId: String, text: String, timestamp: Long) {
        val updates = mutableMapOf<String, Any?>()
        updates["$CHATS/${chatId}/$LAST_MESSAGE"] = text
        updates["$CHATS/${chatId}/$LAST_SENDER_ID"] = senderId
        updates["$CHATS/${chatId}/$LAST_TIMESTAMP"] = timestamp
        databaseRef.updateChildren(updates)
    }

    override suspend fun editMessage(chatId: String, messageId: String, text: String) {
        val updates = mutableMapOf<String, Any?>()
        updates["$MESSAGES/$chatId/$messageId/$TEXT"] = text
        updates["$MESSAGES/$chatId/$messageId/$EDIT"] = true
        databaseRef.updateChildren(updates).await()

        val lastMessageQuery = databaseRef
            .child("$MESSAGES/$chatId")
            .orderByChild(TIMESTAMP)
            .limitToLast(1)

        val lastMessageSnapshot = lastMessageQuery.get().await()
        if (lastMessageSnapshot.exists()) {
            lastMessageSnapshot.children.forEach { it ->
                if (it.key == messageId) {
                    val text = it.child(TEXT).getValue(String::class.java) ?: return@forEach
                    val senderId =
                        it.child(SENDER_ID).getValue(String::class.java) ?: return@forEach
                    val timestamp = it.child(TIMESTAMP).getValue(Long::class.java) ?: return@forEach
                    updateChatPreview(chatId, senderId, text, timestamp)
                }
            }
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> =
        callbackFlow {
            val ref = databaseRef.child(MESSAGES).child(chatId)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = snapshot.children.mapNotNull {
                        val senderId = it.child(SENDER_ID).getValue(String::class.java)
                        val text = it.child(TEXT).getValue(String::class.java)
                        val timestamp = it.child(TIMESTAMP).getValue(Long::class.java)
                        val isEdit = it.child(EDIT).getValue(Boolean::class.java)

                        if (senderId != null && text != null && timestamp != null) {
                            Message(
                                messageId = it.key ?: "",
                                senderId = senderId,
                                text = text,
                                timestamp = timestamp,
                                isEdit = isEdit
                            )
                        } else null
                    }

                    trySend(messages)
                }

                override fun onCancelled(error: DatabaseError) {}

            }

            ref.addValueEventListener(listener)

            awaitClose {
                ref.removeEventListener(listener)
            }
        }

    override fun observeUserChats(uid: String): Flow<List<Chat>> =
        callbackFlow {
            val ref = databaseRef.child(USER_CHATS).child(uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatIds = snapshot.children.mapNotNull { it.key }
                    val chats = mutableListOf<Chat>()

                    chatIds.forEach { chatId ->
                        databaseRef.child(CHATS)
                            .child(chatId)
                            .get()
                            .addOnSuccessListener { chatSnap ->
                                val participants =
                                    chatSnap.child(PARTICIPANTS).children.mapNotNull { it.key }
                                val lastSenderId =
                                    chatSnap.child(LAST_SENDER_ID).getValue(String::class.java)
                                val lastMessage =
                                    chatSnap.child(LAST_MESSAGE).getValue(String::class.java)
                                val lastTimestamp =
                                    chatSnap.child(LAST_TIMESTAMP).getValue(Long::class.java)

                                chats.add(
                                    PrivateChat(
                                        chatId = chatId,
                                        lastSenderId = lastSenderId,
                                        participants = participants,
                                        lastMessage = lastMessage,
                                        lastTimestamp = lastTimestamp
                                    )
                                )
                                trySend(chats)
                            }

                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            }
            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
            }
        }

    override fun observeUnreadChatsCount(uid: String): Flow<Int> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0

                snapshot.children.forEach { chatSnap ->
                    val unread = chatSnap.child(uid)
                        .child(UNREAD_COUNT)
                        .getValue(Int::class.java) ?: 0
                    if (unread > 0) count++
                }

                trySend(count)
            }

            override fun onCancelled(error: DatabaseError) {}

        }
        databaseRef.child(CHAT_MEMBERS).addValueEventListener(listener)

        awaitClose {
            databaseRef.child(CHAT_MEMBERS).removeEventListener(listener)
        }
    }

    override fun observeUnreadMessages(
        uid: String,
        chatId: String
    ): Flow<Int> = callbackFlow {

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val count = snapshot
                    .child(chatId)
                    .child(uid)
                    .child(UNREAD_COUNT)
                    .getValue(Int::class.java) ?: 0

                trySend(count)
            }

            override fun onCancelled(error: DatabaseError) {}

        }
        databaseRef.child(CHAT_MEMBERS).addValueEventListener(listener)

        awaitClose {
            databaseRef.child(CHAT_MEMBERS).removeEventListener(listener)
        }
    }

    override suspend fun resetUnreadCount(uid: String, chatId: String) {

        databaseRef
            .child(CHAT_MEMBERS)
            .child(chatId)
            .child(uid)
            .updateChildren(
                mapOf(
                    UNREAD_COUNT to 0
                )
            ).await()

    }

    companion object {
        const val CHAT_MEMBERS = "chatMembers"
        const val UNREAD_COUNT = "unreadCount"

        const val USER_CHATS = "userChats"
        const val CHATS = "chats"
        const val PARTICIPANTS = "participants"
        const val LAST_MESSAGE = "lastMessage"
        const val LAST_TIMESTAMP = "lastTimestamp"
        const val LAST_SENDER_ID = "lastSenderId"

        const val MESSAGES = "messages"
        const val SENDER_ID = "senderId"
        const val TEXT = "text"
        const val TIMESTAMP = "timestamp"

        const val EDIT = "edit"
    }
}