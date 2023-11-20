package com.example.todoapp.ui.main.taskdescription

import androidx.lifecycle.ViewModel
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskDescriptionViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
):ViewModel() {
    fun getTask(taskId: Int): Task? = tasksRepository.getTask(taskId)

}