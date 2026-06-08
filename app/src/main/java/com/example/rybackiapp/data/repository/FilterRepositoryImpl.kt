package com.example.rybackiapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.rybackiapp.di.FilterDataStore
import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    @FilterDataStore private val dataStore: DataStore<Preferences>
) : FilterRepository {
    override fun getFilter(): Flow<Filter> = dataStore.data.map { prefs ->
        Filter(
            minAge = prefs[MIN_AGE_KEY] ?: 14,
            maxAge = prefs[MAX_AGE_KEY] ?: 100,
            unwantedInterests = prefs[UNWANTED_INTERESTS_KEY] ?: emptySet(),
            desirableInterests = prefs[DESIRABLE_INTERESTS_KEY] ?: emptySet()
        )
    }

    override suspend fun setFilter(filer: Filter) {
        dataStore.edit { prefs ->
            prefs[MAX_AGE_KEY] = filer.maxAge
            prefs[MIN_AGE_KEY] = filer.minAge
            prefs[UNWANTED_INTERESTS_KEY] = filer.unwantedInterests
            prefs[DESIRABLE_INTERESTS_KEY] = filer.desirableInterests

        }
    }

    companion object {
        private val MAX_AGE_KEY = intPreferencesKey("max_age")
        private val MIN_AGE_KEY = intPreferencesKey("min_age")
        private val DESIRABLE_INTERESTS_KEY = stringSetPreferencesKey("desirable_interests")
        private val UNWANTED_INTERESTS_KEY = stringSetPreferencesKey("unwanted_interests")
    }
}
