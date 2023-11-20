package com.example.todoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.ui.main.adapter.TaskAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepo:TasksRepository
):ViewModel() {
    private val _tasksList = MutableLiveData<List<TaskAdapter.TaskItem>>()
    private val _completedTasksList = MutableLiveData<List<TaskAdapter.TaskItem>>()
    val tasksList: LiveData<List<TaskAdapter.TaskItem>> get() = _tasksList
    val completedTasksLis: LiveData<List<TaskAdapter.TaskItem>> get() = _completedTasksList


    fun initData() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect { tasks ->
                tasks?.let {
                    val taskItems = tasks.map { TaskAdapter.TaskItem.Task(it) }
                    _tasksList.value = taskItems
                }
            }
        }

        viewModelScope.launch {
            tasksRepo.getAllTasks().collect { tasks ->
                tasks?.let {
                    val taskItems = tasks.map { TaskAdapter.TaskItem.Task(it) }
                    _tasksList.value = taskItems
                }
            }
        }
    }

    fun addNewTask(title: String, description: String, urgent: Boolean, selectedDate: Date) {
        val task = Task(0,title,description,selectedDate,urgent,false)
        viewModelScope.launch { tasksRepo.addNewTask(task) }
    }

    fun onCompletedChanged(task: Task) {
        if(task.isCompleted){
            tasksRepo.addToCompleted(task)
        }else{
            tasksRepo.removeFromCompleted(task)
        }
    }

    fun getTask(taskId: Int): Task? {
        return _tasksList.value?.mapNotNull { it ->
            when (it) {
                is TaskAdapter.TaskItem.Task -> it.task
                else -> null
            }
        }?.firstOrNull { it.id == taskId }
    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        tasksRepo.deleteTask(task)
    }
}