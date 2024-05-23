package com.example.todoapp.ui.main

import com.example.todoapp.model.Task

interface PostDetailListener {
    fun onCardViewClicked(task: Task)
}