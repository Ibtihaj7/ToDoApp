package com.example.todoapp.ui.main

import com.example.todoapp.model.Task

interface CompletedChangeListener {
    fun onCompletedChanged(task: Task)
}