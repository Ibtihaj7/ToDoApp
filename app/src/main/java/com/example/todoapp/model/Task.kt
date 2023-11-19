package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todoapp.utils.Constant.TASKS_TABLE
import java.util.Date

@Entity(tableName = TASKS_TABLE)
@TypeConverters(DateConverter::class)
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val dueDate:Date,
    val urgent:Boolean = false,
    val isCompleted:Boolean = false
)