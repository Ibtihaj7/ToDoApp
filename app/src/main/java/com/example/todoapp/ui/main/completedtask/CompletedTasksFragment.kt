package com.example.todoapp.ui.main.completedtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCompletedTasksBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.MainViewModel
import com.example.todoapp.ui.main.PostDetailListener
import com.example.todoapp.ui.main.adapter.TaskAdapter
import com.example.todoapp.ui.main.home.AllTasksFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedTasksFragment : Fragment() ,CompletedChangeListener,PostDetailListener{
    private lateinit var binding:FragmentCompletedTasksBinding
    private lateinit var tasksAdapter: TaskAdapter
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedTasksBinding.inflate(inflater)
        initUI()

        observeTasksList()
        return binding.root
    }

    private fun initUI() {
        navController=findNavController()
        tasksAdapter = TaskAdapter(this, this)
        binding.completedRv.layoutManager = LinearLayoutManager(requireContext())
        binding.completedRv.adapter = tasksAdapter

    }

    private fun observeTasksList() {
        mainViewModel.tasksList.observe(viewLifecycleOwner) { tasks ->
            val completedTasks = tasks
                .filterIsInstance<TaskAdapter.TaskItem.Task>()
                .filter { it.task.isCompleted }

            tasksAdapter.updateData(completedTasks.map { TaskAdapter.TaskItem.Task(it.task) })
        }
    }


    override fun onCompletedChanged(task: Task) {
        mainViewModel.onCompletedChanged(task)
    }

    override fun onCardViewClicked(task: Task) {
        val action = CompletedTasksFragmentDirections.actionCompletedTasksFragmentToTaskDescriptionFragment(task.id)
        navController.navigate(action)
    }

}