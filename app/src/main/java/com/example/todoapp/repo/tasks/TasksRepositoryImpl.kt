package com.example.todoapp.repo.tasks

import android.util.Log
import com.example.todoapp.model.Task
import com.example.todoapp.repo.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val dbRepository: DatabaseRepository
) : TasksRepository{
    private val tasks: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())
    private val completedTasksId: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    private val urgentTasksId:MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    private val othersTasksId:MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())


    override suspend fun addNewTask(task:Task): Unit = withContext(Dispatchers.IO){
        try{
            dbRepository.insertNewTask(task)
            tasks.value += task

            if(task.urgent) urgentTasksId.value += task.id else othersTasksId.value += task.id

        } catch (e: Exception) {
            Log.e("PostRepoImpl", "Error in getPosts: ${e.message}", e)
        }
    }


    override fun getCompletedTasks():Flow<List<Task>> = tasks.map {
        it.filter { task ->
            completedTasksId.value.contains(task.id)
        }
      }

    override fun getUrgentTasks():Flow<List<Task>> = tasks.map {
        it.filter { task ->
            urgentTasksId.value.contains(task.id)
        }
    }

    override fun getOthersTasks():Flow<List<Task>> = tasks.map {
        it.filter { task ->
            othersTasksId.value.contains(task.id)
        }
    }

    override suspend fun getAllTasks(): Flow<List<Task>> {
        return dbRepository.getAllTasks()
    }

    override fun addToUrgent(task: Task) {
        urgentTasksId.value += task.id
    }

    override fun addToOthers(task: Task) {
        othersTasksId.value += task.id
    }

    override fun addToCompleted(task: Task) {
        completedTasksId.value += task.id
    }

    override fun removeFromCompleted(task: Task) {
        completedTasksId.value -= task.id
    }

    override fun getTask(taskId: Int): Task? = tasks.value.firstOrNull { task ->
        task.id == taskId
    }

    override suspend fun deleteTask(task: Task) {
        dbRepository.deleteTask(task)
    }

}