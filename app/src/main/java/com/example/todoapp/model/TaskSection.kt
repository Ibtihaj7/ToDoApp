package com.example.todoapp.model

data class TaskSection (
    private val title:String,
    private val tasks:List<Task>
){
    fun getTitle() = title
    fun getTasks() = tasks
}