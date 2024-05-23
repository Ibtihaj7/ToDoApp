package com.example.todoapp.repo.tasks

import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun addNewTask(task: Task)
    suspend fun getCompletedTasks():Flow<List<Task>>
    suspend fun getAllTasks():Flow<List<Task>>
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getTasksWithDueDateUpcoming(): List<Task>
    suspend fun getTasksWithDueDatePassed(): List<Task>
}