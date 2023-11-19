package com.example.todoapp.repo.database

import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepositoryImpl @Inject constructor(
    private val dao: TaskDAO
) :DatabaseRepository {
    override suspend fun insertNewTask(task: Task) = dao.insertNewTask(task)
    override suspend fun deleteTask(task: Task) = dao.deleteTask(task)
    override suspend fun getAllTasks() = dao.getTasks()
}