package com.example.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.todoapp.utils.Constant.TASKS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(DateConverter::class)
interface TaskDAO {
    @Insert
    suspend fun insertNewTask(task:Task)
    @Delete
    suspend fun deleteTask(task: Task)
    @Query("SELECT * FROM $TASKS_TABLE")
    fun getTasks(): Flow<List<Task>>
    @Query("SELECT * FROM $TASKS_TABLE WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>
    @Update
    suspend fun updateTask(task: Task)
    @Query("SELECT * FROM $TASKS_TABLE WHERE dueDate >= :currentDate")
    suspend fun getTasksWithDueDateUpcoming(currentDate: Long): List<Task>
    @Query("SELECT * FROM $TASKS_TABLE WHERE dueDate < :currentDate")
    suspend fun getTasksWithDueDatePassed(currentDate: Long): List<Task>
}