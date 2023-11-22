package com.example.todoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.ui.main.adapter.TaskAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
                tasks.let {
                    val taskItems = tasks.map { TaskAdapter.TaskItem.Task(it) }
                    _tasksList.value = taskItems
                }
            }
        }

        viewModelScope.launch {
            tasksRepo.getCompletedTasks().collect { tasks ->
                tasks.let {
                    val taskItems = tasks.map { TaskAdapter.TaskItem.Task(it) }
                    _completedTasksList.value = taskItems
                }
            }
        }
    }

    fun addNewTask(task: Task) {
        viewModelScope.launch { tasksRepo.addNewTask(task) }
    }

    fun onCompletedChanged(task: Task) = viewModelScope.launch {
        val taskCopy = task.copy(isCompleted = !(task.isCompleted))
        tasksRepo.updateTask(taskCopy)
    }

    fun getTask(taskId: Int): Task? {
        return _tasksList.value?.mapNotNull {
            when (it) {
                is TaskAdapter.TaskItem.Task -> it.task
                else -> null
            }
        }?.firstOrNull { it.id == taskId }
    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        tasksRepo.deleteTask(task)
    }

    fun getTasksWithDueDateUpcoming() = viewModelScope.launch {
        val tasks = tasksRepo.getTasksWithDueDateUpcoming()
        val tasksItem = tasks.map { TaskAdapter.TaskItem.Task(it) }
        _tasksList.value = tasksItem
    }
    fun getTasksWithDueDatePassed()= viewModelScope.launch {
        val tasks = tasksRepo.getTasksWithDueDatePassed()
        val tasksItem = tasks.map { TaskAdapter.TaskItem.Task(it) }
        _tasksList.value = tasksItem
    }

    fun filterList(query: String?) {
        viewModelScope.launch {
            tasksRepo.getAllTasks()
                .onEach { posts ->
                    val filteredPosts = if (query.isNullOrBlank()) {
                        posts
                    } else {
                        posts.filter { post ->
                            post.title.contains(query, ignoreCase = true)
                        }
                    }
                    _tasksList.value = filteredPosts.map { TaskAdapter.TaskItem.Task(it) }
                }
                .collect()
        }
    }

}