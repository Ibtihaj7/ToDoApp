package com.example.todoapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constant {
    const val TASKS_DATABASE="tasksDatabase"
    const val TASKS_TABLE="task"
    const val DATASTORE_NAME = "filter status"
    val UPCOMING_KEY = booleanPreferencesKey("upcoming")
    val PAST_DUE_KEY = booleanPreferencesKey("pastDue")
    val ALL_TASKS_KEY = booleanPreferencesKey("allTasks")
}