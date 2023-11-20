package com.example.todoapp.ui.main.addnewtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddNewTaskViewModel @Inject constructor(
    private val tasksRepo:TasksRepository
):ViewModel() {


}