package com.example.rybackiapp.data.repository

import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class UserIdCacheRepositoryImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : UserIdCacheRepository {

    private var _cacheUserId: String? = null

    override fun observeUserId(): Flow<String> = flow {
        emit(_cacheUserId ?: saveUserId().first())
    }


    private fun saveUserId(): Flow<String> = flow {
        profileRepository.observeUserId().collect {
            _cacheUserId = it
        }
        emit(_cacheUserId!!)
    }

    override fun onUserLogout() {
        _cacheUserId = null
    }
}

