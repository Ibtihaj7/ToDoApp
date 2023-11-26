package com.example.todoapp.repo.database

import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun insertNewTask(task: Task)
    suspend fun deleteTask(task: Task)
    fun getAllTasks(): Flow<List<Task>>
    fun getCompletedTasks():Flow<List<Task>>
    suspend fun updateTask(task: Task)
    suspend fun getTasksWithDueDateUpcoming(): List<Task>
    suspend fun getTasksWithDueDatePassed(): List<Task>
}