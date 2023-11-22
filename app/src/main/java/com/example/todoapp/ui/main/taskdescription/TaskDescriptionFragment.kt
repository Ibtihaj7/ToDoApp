package com.example.todoapp.ui.main.taskdescription

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskDescriptionBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDescriptionFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding:FragmentTaskDescriptionBinding
    private val args: TaskDescriptionFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDescriptionBinding.inflate(inflater)

        setNavigationOnClickListener()
        val taskId = args.taskId
        val task = mainViewModel.getTaskById(taskId)
        context = requireContext()

        if(task.isCompleted){
            binding.materialDivider.visibility = View.GONE
            binding.completeBtn.visibility = View.GONE
        }
        binding.urgentImageView.visibility = if(!task.urgent) View.GONE else View.VISIBLE

        navController = findNavController()

        setDeleteTask(task!!)
        setCompletedTask(task)

        binding.task = task
        return binding.root
    }

    private fun setCompletedTask(task: Task) {
        binding.completeBtn.setOnClickListener {
            mainViewModel.onCompletedChanged(task)
            showTaskAddedSnackBar(getSuccessfullyTaskCompleted(context))
            navigateToAllTasksFragment()
        }
    }

    private fun setDeleteTask(task: Task) {
        binding.taskDelete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.confirmation_title)
                .setMessage(R.string.confirmation_message)
                .setNegativeButton(R.string.decline) { _, _ -> }

            val dialog = builder.setPositiveButton(R.string.accept, null).show()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
                mainViewModel.deleteTask(task)
                showTaskAddedSnackBar(getSuccessfullyTaskAdded(context))
                navigateToAllTasksFragment()
                dialog.cancel()
            }
        }
    }

    private fun showTaskAddedSnackBar(message:String) {
        Snackbar.make(
            binding.root,
            message,
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
        private fun getSuccessfullyTaskAdded(context: Context) =  context.getString(R.string.successfully_task_deleted)
        private fun getSuccessfullyTaskCompleted(context: Context) = context.getString(R.string.successfully_task_completed)
    }
}