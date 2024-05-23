package com.example.todoapp.repo.tasks

import com.example.todoapp.model.Task
import com.example.todoapp.repo.database.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val dbRepository: DatabaseRepository
) : TasksRepository{
    override suspend fun addNewTask(task:Task) = dbRepository.insertNewTask(task)
    override suspend fun getCompletedTasks():Flow<List<Task>> = dbRepository.getCompletedTasks()
    override suspend fun getAllTasks(): Flow<List<Task>> = dbRepository.getAllTasks()
    override suspend fun deleteTask(task: Task) = dbRepository.deleteTask(task)
    override suspend fun updateTask(task: Task) = dbRepository.updateTask(task)
    override suspend fun getTasksWithDueDateUpcoming() = dbRepository.getTasksWithDueDateUpcoming()
    override suspend fun getTasksWithDueDatePassed() = dbRepository.getTasksWithDueDatePassed()
}