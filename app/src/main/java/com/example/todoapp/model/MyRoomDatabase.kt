package com.example.todoapp.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDAO
}