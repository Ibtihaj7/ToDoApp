package com.example.todoapp.ui.main.addnewtask

import androidx.lifecycle.ViewModel

class AddNewTaskViewModel:ViewModel() {
    fun validateInput(title: String, description: String,  selectedDate: String) =
         !(title.isEmpty() || description.isEmpty() || selectedDate.isEmpty())

    fun addNewTask(title: String, description: String, urgent: Boolean, selectedDate: String) {

    }

}