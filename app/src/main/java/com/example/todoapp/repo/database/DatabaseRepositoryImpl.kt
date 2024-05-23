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
    override fun getAllTasks() = dao.getTasks()
    override fun getCompletedTasks() = dao.getCompletedTasks()
    override suspend fun updateTask(task: Task) = dao.updateTask(task)
    override suspend fun getTasksWithDueDateUpcoming(): List<Task>{
        val currentDateInMillis = System.currentTimeMillis()
        return dao.getTasksWithDueDateUpcoming(currentDateInMillis)
    }
    override suspend fun getTasksWithDueDatePassed(): List<Task> {
        val currentDateInMillis = System.currentTimeMillis()
        return dao.getTasksWithDueDatePassed(currentDateInMillis)
    }
}