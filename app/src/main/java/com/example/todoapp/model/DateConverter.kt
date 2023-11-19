package com.example.todoapp.model

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(milliseconds: Long?): Date? {
        return if (milliseconds != null) Date(milliseconds) else null
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}