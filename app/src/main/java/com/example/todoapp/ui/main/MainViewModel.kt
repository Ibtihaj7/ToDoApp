package com.example.todoapp.ui.main

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.ui.main.adapter.TaskAdapter
import com.example.todoapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
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

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    init {
        initData()
    }

    fun initData() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect { tasks ->
                updateTasksListBasedOnPreferences(tasks)
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

    private suspend fun updateTasksListBasedOnPreferences(tasks: List<Task>) = viewModelScope.launch{
        try {
            val preferences = dataStore.data.first()

            val upcomingChecked = preferences[Constant.UPCOMING_KEY] ?: false
            val pastDueChecked = preferences[Constant.PAST_DUE_KEY] ?: false

            if(upcomingChecked) _tasksList.value = tasksRepo.getTasksWithDueDateUpcoming().map { TaskAdapter.TaskItem.Task(it) }
            else if(pastDueChecked) _tasksList.value = tasksRepo.getTasksWithDueDatePassed().map { TaskAdapter.TaskItem.Task(it) }
            else _tasksList.value = tasks.map { TaskAdapter.TaskItem.Task(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Log.d("main",e.message.toString())
        }
    }


    fun addNewTask(task: Task) {
        updateDataStorePreferences()
        viewModelScope.launch { tasksRepo.addNewTask(task) }
    }

    fun onCompletedChanged(task: Task) = viewModelScope.launch {
        val taskCopy = task.copy(isCompleted = !(task.isCompleted))
        tasksRepo.updateTask(taskCopy)
    }

    fun getTaskById(taskId: Int): Task {
        return _tasksList.value!!.map {
            when (it) {
                is TaskAdapter.TaskItem.Task -> it.task
                else -> Task(0,"","", Date())
            }
        }.first { it.id == taskId }
    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        tasksRepo.deleteTask(task)
    }

    fun filterList(query: String?) {
        viewModelScope.launch {
            updateDataStorePreferences()
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

    private fun updateDataStorePreferences() = viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[Constant.UPCOMING_KEY] = false
                preferences[Constant.PAST_DUE_KEY] = false
                preferences[Constant.ALL_TASKS_KEY] = false
            }
        }

    fun saveFilterStatus(pastDue: Boolean, upcoming: Boolean,allTasks:Boolean)= viewModelScope.launch {
        try {
            dataStore.edit { preferences ->
                preferences[Constant.UPCOMING_KEY] = upcoming
                preferences[Constant.PAST_DUE_KEY] = pastDue
                preferences[Constant.ALL_TASKS_KEY] = allTasks
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}