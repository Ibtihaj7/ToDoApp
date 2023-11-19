package com.example.todoapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepo:TasksRepository
):ViewModel() {
    private val _tasksList = MutableLiveData<List<Task>>()
    val tasksList: LiveData<List<Task>> get() = _tasksList

    init {
        updateTasksList()
    }

    private fun updateTasksList() = viewModelScope.launch {
        tasksRepo.getAllTasks().catch { exception ->
            Log.d("MainViewModel", "${exception.message}")
        }.collect { tasks ->
            _tasksList.value = tasks
        }
    }

}