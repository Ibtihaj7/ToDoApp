package com.example.todoapp.repo.tasks

import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun addNewTask(task: Task)
    fun getCompletedTasks():Flow<List<Task>>
    fun getUrgentTasks():Flow<List<Task>>
    fun getOthersTasks():Flow<List<Task>>
    suspend fun getAllTasks():Flow<List<Task>>
    fun addToUrgent(task: Task)
    fun addToOthers(task: Task)
    fun addToCompleted(task: Task)
    fun removeFromCompleted(task: Task)
    fun getTask(taskId: Int): Task?
    suspend fun deleteTask(task: Task)
}