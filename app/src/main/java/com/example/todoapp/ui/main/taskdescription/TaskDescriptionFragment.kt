package com.example.todoapp.ui.main.taskdescription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskDescriptionBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.MainViewModel
import com.example.todoapp.ui.main.addnewtask.AddNewTaskFragment
import com.example.todoapp.ui.main.addnewtask.AddNewTaskFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class TaskDescriptionFragment : Fragment() {
    private val viewModel:TaskDescriptionViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding:FragmentTaskDescriptionBinding
    private val args: TaskDescriptionFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDescriptionBinding.inflate(inflater)

        setNavigationOnClickListener()
        val taskId = args.taskId
        val task = mainViewModel.getTask(taskId)
        navController = findNavController()

        setDeleteTask(task!!)

        binding.task = task
        return binding.root
    }

    private fun setDeleteTask(task: Task) {
        binding.taskDelete.setOnClickListener {
            mainViewModel.deleteTask(task)
            showTaskAddedSnackBar()
            navigateToAllTasksFragment()
        }
    }

    private fun showTaskAddedSnackBar() {
        Snackbar.make(
            binding.root,
            SUCCESSFULLY_TASK_DELETED,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateToAllTasksFragment() {
        val action = TaskDescriptionFragmentDirections.actionTaskDescriptionFragmentToAllTasksFragment()
        navController.navigate(action)
    }
    private fun setNavigationOnClickListener() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            val action = TaskDescriptionFragmentDirections.actionTaskDescriptionFragmentToAllTasksFragment()
            navController.navigate(action)
        }
    }

    companion object{
        private const val SUCCESSFULLY_TASK_DELETED = "Task deleted successfully"
    }
}