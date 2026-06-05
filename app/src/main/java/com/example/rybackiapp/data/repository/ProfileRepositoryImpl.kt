package com.example.rybackiapp.data.repository

import android.util.Log
import com.example.rybackiapp.data.repository.ChatRepositoryImpl.Companion.PARTICIPANTS
import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.model.MainProfile
import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.utils.TagNameAlreadyExistsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.notifyAll
import javax.inject.Inject
import kotlin.String
import kotlin.math.log

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val databaseRef: DatabaseReference
) : ProfileRepository {
    override suspend fun getUserProfile(): Result<MainProfile> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("User not authorized"))
            val snapshot = databaseRef.child(USERS).child(user.uid).get().await()

            val name = snapshot.child(NAME).getValue(String::class.java)
            Log.d("Account", "-getUserProfile: $name")
            val year = snapshot.child(YEAR).getValue(String::class.java)

            val tagName = snapshot.child(TAG_NAME).getValue(String::class.java)
            val interests = snapshot.child(INTERESTS).children.mapNotNull { it.key?.toInt() }

            Log.d("KAK DCE ", "getUserProfile: $interests")

            Log.d("Account", "--getUserProfile: $name")
            val photoUrl =
                snapshot.child(PHOTO_URL).getValue(String::class.java) ?: user.photoUrl?.toString()


            Result.success(
                MainProfile(
                    id = user.uid,
                    name = name,
                    email = user.email,
                    tagName = tagName,
                    interests = interests,
                    year = year,
                    mainPhotoUrl = photoUrl
                )
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserProfile(
        id: String,
        name: String,
        year: String,
        tagName: String
    ): Result<Unit> {

        return try {

//            val tagOwner = databaseRef
//                .child(TAG_NAMES)
//                .child(tagName)
//                .get()
//                .await()
//                .getValue(String::class.java)

            //if (tagOwner != null && tagOwner != id) throw IllegalStateException("Tag '$tagName' is already taken")
            if (checkTagName(
                    id,
                    tagName
                )
            ) throw TagNameAlreadyExistsException("Tag '$tagName' is already taken")
            databaseRef.child(USERS)
                .child(id)
                .setValue(
                    mapOf(
                        NAME to name,
                        YEAR to year,
                        TAG_NAME to tagName,
                        PHOTO_URL to ""
                    )
                ).await()

            databaseRef.child(TAG_NAMES)
                .child(tagName)
                .setValue(id)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun checkTagName(id: String, tagName: String): Boolean {
        val tagOwner = databaseRef
            .child(TAG_NAMES)
            .child(tagName)
            .get()
            .await()
            .getValue(String::class.java)
        return tagOwner != null && tagOwner != id
    }

    override suspend fun editUserProfile(
        id: String,
        name: String,
        year: String,
        tagName: String,
        interests: Set<InterestItem>?
    ): Result<Unit> {
        return try {
            val snapshot = databaseRef.child(USERS).child(id).get().await()
            if (checkTagName(id, tagName)) throw TagNameAlreadyExistsException("Tag '$tagName' is already taken")
            val updates = mutableMapOf<String, Any>()

            updates["$USERS/$id/$NAME"] = name
            updates["$USERS/$id/$YEAR"] = year
            updates["$USERS/$id/$TAG_NAME"] = tagName


            val oldTagName = snapshot.child(TAG_NAME).getValue(String::class.java) ?: ""
            databaseRef
                .child(TAG_NAMES)
                .child(oldTagName)
                .removeValue()
                .await()

            databaseRef
                .child(TAG_NAMES)
                .child(tagName)
                .setValue(id)
                .await()


            if (interests.isNullOrEmpty()) {
                databaseRef.child(USERS)
                    .child(id)
                    .child(INTERESTS)
                    .removeValue()
                    .await()
            } else {
                val interestsData = interests.associate { it.id.toString() to true }
                databaseRef.child(USERS)
                    .child(id)
                    .child(INTERESTS)
                    .setValue(interestsData)
                    .await()
            }

            if (updates.isNotEmpty()) {
                databaseRef.updateChildren(updates).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeUserUserProfile(): Flow<MainProfile> = callbackFlow {
        val user = auth.currentUser ?: return@callbackFlow

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(NAME).getValue(String::class.java)
                val year = snapshot.child(YEAR).getValue(String::class.java)
                val photoUrl = snapshot.child(PHOTO_URL).getValue(String::class.java)
                    ?: user.photoUrl?.toString()

                val tagName = snapshot.child(TAG_NAME).getValue(String::class.java)
                val interests = snapshot.child(INTERESTS).children.mapNotNull { it.key?.toInt() }

                val profile = MainProfile(
                    id = user.uid,
                    name = name,
                    email = user.email,
                    tagName = tagName,
                    interests = interests,
                    year = year,
                    mainPhotoUrl = photoUrl
                )

                trySend(profile) // Отправляем новое значение
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        databaseRef.child(USERS).child(user.uid).addValueEventListener(listener)

        awaitClose {
            databaseRef.child(USERS).child(user.uid).removeEventListener(listener)
        }
    }

    override suspend fun uploadMainImage(id: String, url: String) {
        if (url.isBlank()) {
            Log.e("FIREBASE", "URL пустой! Не сохраняем")
        }
        databaseRef.child(USERS)
            .child(id)
            .updateChildren(mapOf(PHOTO_URL to (url))).await()
    }

    override suspend fun observeUserId(): Flow<String> =
        flow {
            emit(auth.currentUser?.uid ?: throw Exception("No userId"))
        }

    private companion object {
        const val USERS = "users"
        const val NAME = "name"
        const val YEAR = "year"
        const val PHOTO_URL = "photoUrl"
        const val TAG_NAME = "tagName"
        const val TAG_NAMES = "tagNames"
        const val INTERESTS = "interests"
    }

}