package com.example.todoapp.ui.main.bottomsheet

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor():ViewModel() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val _upcomingStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _pastDueStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _allTasksStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val upcomingStatus: LiveData<Boolean> get() = _upcomingStatus
    val pastDueStatus: LiveData<Boolean> get() = _pastDueStatus
    val allTasksStatus: LiveData<Boolean> get() = _allTasksStatus
    init {
        checkPreferences()
    }
    fun checkPreferences() {
        upcomingChecked()
        pastDueChecked()
        allTasksChecked()
    }

    private fun upcomingChecked() = viewModelScope.launch {
        val preferences = dataStore.data.first()
         _upcomingStatus.value = preferences[Constant.UPCOMING_KEY] ?: false
    }

    private fun pastDueChecked() = viewModelScope.launch {
        val preferences = dataStore.data.first()
        _pastDueStatus.value = preferences[Constant.PAST_DUE_KEY] ?: false
    }

    private fun allTasksChecked() = viewModelScope.launch {
        val preferences = dataStore.data.first()
        _allTasksStatus.value = preferences[Constant.ALL_TASKS_KEY] ?: false
    }
}