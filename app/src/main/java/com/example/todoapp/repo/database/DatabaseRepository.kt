package com.example.todoapp.repo.database

import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun insertNewTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getAllTasks(): Flow<List<Task>>
}