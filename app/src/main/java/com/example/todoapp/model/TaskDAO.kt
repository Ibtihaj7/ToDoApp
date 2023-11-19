package com.example.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.todoapp.utils.Constant.TASKS_TABLE

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertNewTask(task:Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM $TASKS_TABLE")
    fun getTasks(): Flow<List<Task>>
}