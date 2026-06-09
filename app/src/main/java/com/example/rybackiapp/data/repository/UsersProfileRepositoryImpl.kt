package com.example.rybackiapp.data.repository


import android.util.Log
import com.example.rybackiapp.di.GlobalReference
import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.model.PrivateUserChatPreview
import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.model.UserProfile
import com.example.rybackiapp.domain.model.UserProfileList
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject
import kotlin.String

class UsersProfileRepositoryImpl @Inject constructor(
    @GlobalReference private val databaseRef: DatabaseReference
) : UsersProfileRepository {
    override suspend fun getUsersProfileList(): Result<UserProfileList> {
        return try {

            val snapshot = databaseRef.child(USERS).get().await()

            val user = if (snapshot.exists()) {
                snapshot.children.mapNotNull { childSnapshot ->
                    val id = childSnapshot.key ?: ""
                    val name = childSnapshot.child(NAME).getValue(String::class.java)
                    val year = childSnapshot.child(YEAR).getValue(String::class.java)
                    val photoUrl = childSnapshot.child(PHOTO_URL).getValue(String::class.java)
                    UserProfile(
                        id = id,
                        name = name,
                        year = year,
                        mainPhotoUrl = photoUrl,
                    )
                }
            } else emptyList()

            Result.success(UserProfileList(user))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsersProfile(id: String): Result<UserProfile> {
        return try {
            val snapshot = databaseRef.child(USERS).child(id).get().await()
            val name = snapshot.child(NAME).getValue(String::class.java)
            val year = snapshot.child(YEAR).getValue(String::class.java)
            val photoUrl = snapshot.child(PHOTO_URL).getValue(String::class.java)
            val tagName = snapshot.child(TAG_NAME).getValue(String::class.java)
            val interests = snapshot.child(INTERESTS).children.mapNotNull { it.key?.toInt() }
            Result.success(
                UserProfile(
                    id = id,
                    name = name,
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

    override suspend fun searchUsersProfileList(
        search: String,
        filter: Filter
    ): UserProfileList {

        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        val currentYear = calendar.get(Calendar.YEAR)

        val snapshot = databaseRef.child(USERS).get().await()

        val user = if (snapshot.exists()) {
            snapshot.children.mapNotNull { childSnapshot ->
                val id = childSnapshot.key ?: ""

                val name = childSnapshot.child(NAME).getValue(String::class.java)
                val year = childSnapshot.child(YEAR).getValue(String::class.java)
                val photoUrl = childSnapshot.child(PHOTO_URL).getValue(String::class.java)
                val interests = childSnapshot.child(INTERESTS).children.mapNotNull { it.key }
                val age = currentYear - (year?.toIntOrNull() ?: currentYear)

                val isNameSearch = name?.startsWith(search, ignoreCase = true) ?: false
                val isAge = age >= filter.minAge && age <= filter.maxAge
                val isDesirableInterests = if (filter.desirableInterests.isEmpty()) {
                    true
                } else {
                    filter.desirableInterests.any { it in interests }
                }
                val isUnwantedInterests = if (filter.unwantedInterests.isEmpty()) {
                    false
                } else {
                    filter.unwantedInterests.any { it in interests }
                }

                if (isNameSearch && isAge && isDesirableInterests && !isUnwantedInterests) {
                    UserProfile(
                        id = id,
                        name = name,
                        year = year,
                        mainPhotoUrl = photoUrl,
                    )
                } else null
            }
        } else emptyList()

        return UserProfileList(user)
    }

    override suspend fun searchWithTagUsersProfileList(tag: String): UserProfileList {

        val snapshot = databaseRef.child(TAG_NAMES).child(tag).get().await()

        if (snapshot.exists()) {
            val uid = snapshot.getValue(String::class.java)
            uid?.let {
                val snapshot = databaseRef.child(USERS).child(uid).get().await()

                val name = snapshot.child(NAME).getValue(String::class.java)
                val year = snapshot.child(YEAR).getValue(String::class.java)
                val photoUrl = snapshot.child(PHOTO_URL).getValue(String::class.java)

                return UserProfileList(
                    profiles = listOf(
                        UserProfile(
                            id = uid,
                            name = name,
                            year = year,
                            mainPhotoUrl = photoUrl,
                        )
                    )
                )
            }
        }
        return UserProfileList()
    }

    override fun observeUserProfilePreview(id: String): Flow<UserPreview> = flow {

        val snapshot = databaseRef.child(USERS).child(id).get().await()
        val name = snapshot.child(NAME).getValue(String::class.java)
        val photoUrl = snapshot.child(PHOTO_URL).getValue(String::class.java)


        emit(
            UserPreview(
                uid = id,
                name = name ?: "",
                mainPhotoUrl = photoUrl
            )
        )
    }

    override fun observeUserChatProfilePreview(id: String): Flow<PrivateUserChatPreview> = flow {
        val snapshot = databaseRef.child(USERS).child(id).get().await()
        val name = snapshot.child(NAME).getValue(String::class.java)
        val photoUrl = snapshot.child(PHOTO_URL).getValue(String::class.java)
        val status: Boolean? = false


        emit(
            PrivateUserChatPreview(
                uid = id,
                name = name ?: "",
                status = status ?: false,
                mainPhotoUrl = photoUrl
            )
        )
    }

    companion object {
        const val USERS = "users"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val YEAR = "year"
        const val TAG_NAME = "tagName"
        const val TAG_NAMES = "tagNames"
        const val INTERESTS = "interests"
    }
}



