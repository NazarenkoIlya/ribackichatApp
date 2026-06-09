package com.example.rybackiapp.data.repository

import com.example.rybackiapp.di.ConnectedRef
import com.example.rybackiapp.di.GlobalReference
import com.example.rybackiapp.domain.repository.UserStatusRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserStatusRepositoryImpl @Inject constructor(
    @GlobalReference private val databaseRef: DatabaseReference,
    @ConnectedRef private val connectedRef: DatabaseReference
) : UserStatusRepository {
    override fun observeConnectionStatus(): Flow<Boolean> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isConnected = snapshot.getValue(Boolean::class.java) ?: false
                trySend(isConnected)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        connectedRef.addValueEventListener(listener)
        awaitClose { connectedRef.removeEventListener(listener) }
    }

    override suspend fun setUserOnline(userId: String) {
        val userRef = databaseRef.child(USERS).child(userId).child(IS_ONLINE)
        userRef.onDisconnect().setValue(false).await()
        userRef.setValue(true).await()
    }

    override fun observeUserOnline(userId: String): Flow<Boolean> = callbackFlow {
        val ref = databaseRef.child(USERS).child(userId).child(IS_ONLINE)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Boolean::class.java) ?: false)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }


    companion object {
        const val USERS = "users"
        const val IS_ONLINE = "isOnline"
    }
}