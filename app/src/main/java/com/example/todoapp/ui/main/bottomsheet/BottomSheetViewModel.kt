package com.example.todoapp.ui.main.bottomsheet

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.example.todoapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor():ViewModel() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    suspend fun isUpcomingChecked(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Constant.UPCOMING_KEY] ?: false
    }

    suspend fun isPastDueChecked(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Constant.PAST_DUE_KEY] ?: false
    }

    suspend fun isAllTasksChecked(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Constant.ALL_TASKS_KEY] ?: false
    }
}