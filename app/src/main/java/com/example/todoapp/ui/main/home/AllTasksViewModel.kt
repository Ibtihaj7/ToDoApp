package com.example.todoapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(
    private val dbRepository: DatabaseRepository
) : ViewModel() {
    private val _tasksList = MutableLiveData<List<Task>>()
    val tasksList: LiveData<List<Task>> get() = _tasksList

    fun getAllTasks() = viewModelScope.launch {
        dbRepository.getAllTasks().collect {
            _tasksList.value = it
        }
    }
}
