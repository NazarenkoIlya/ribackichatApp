package com.example.rybackiapp.data.repository


import android.util.Log
import androidx.room.Database
import com.example.rybackiapp.domain.model.AuthResult
import com.example.rybackiapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging,
    private val database: DatabaseReference
) : AuthRepository {
    override suspend fun signInEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            firebaseMessaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                val token = task.result

                database.child("users")
                    .child(uid)
                    .child("fcmToken")
                    .setValue(token)

            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpEmail(
        email: String,
        password: String
    ): Result<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID is Null"))

//            firebaseMessaging.token.addOnCompleteListener { task ->
//                if (!task.isSuccessful) return@addOnCompleteListener
//                val token = task.result
//
//                database.child("users")
//                    .child(uid)
//                    .child("fcmToken")
//                    .setValue(token)
//
//            }

            Result.success(AuthResult(uid))
        } catch (e: Exception) {
            Log.e("AAAAA", "signUpEmail: ${e}")
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            val uid =
                firebaseAuth.currentUser?.uid ?: return Result.failure(Exception("UID is Null"))
            firebaseAuth.signOut()
            database.child("users")
                .child(uid)
                .child("fcmToken")
                .removeValue()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserAuthorized(): Boolean {
        return firebaseAuth.currentUser != null
    }
}