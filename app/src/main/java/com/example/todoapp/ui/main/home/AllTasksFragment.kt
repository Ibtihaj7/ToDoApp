package com.example.todoapp.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAllTasksBinding
import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskType
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.MainViewModel
import com.example.todoapp.ui.main.PostDetailListener
import com.example.todoapp.ui.main.adapter.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AllTasksFragment : Fragment(), CompletedChangeListener, PostDetailListener {
    private val viewModel: AllTasksViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAllTasksBinding
    private lateinit var tasksAdapter:TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllTasksBinding.inflate(inflater)
        navController = findNavController()
        setHasOptionsMenu(true)

        val task1 = Task(0, "abrar", "adham", Date(), true, isCompleted = true)
//        val job = viewModel.addTask(task1)

        initUI()
        observeTasksList()

        return binding.root
    }

    private fun initUI() {
        tasksAdapter = TaskAdapter(this, this)
        binding.tasksRv.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRv.adapter = tasksAdapter

        val title1 = TaskType.ARGENT
        val title2 = TaskType.OTHERS

        // Create a list of TaskItem objects
        val taskItems = listOf(
            TaskAdapter.TaskItem.Title(title1)
        )

        // Update the adapter with the list of TaskItem objects
        tasksAdapter.updateData(taskItems)

        binding.addTaskFloatingButton.setOnClickListener {
            val action = AllTasksFragmentDirections.actionAllTasksFragmentToAddNewTaskFragment()
            findNavController().navigate(action)
        }
    }


    private fun observeTasksList() {
        mainViewModel.tasksList.observe(viewLifecycleOwner) { taskItems ->
            taskItems?.let {
                if (::tasksAdapter.isInitialized) {
                    val filteredList = filterTasks(taskItems)
                    tasksAdapter.updateData(filteredList)
                }
            }
        }
    }

    private fun filterTasks(taskItems: List<TaskAdapter.TaskItem>): List<TaskAdapter.TaskItem> {
        val filteredList = mutableListOf<TaskAdapter.TaskItem>()

        // Filter urgent tasks and add title
        val urgentTasks = taskItems.filterIsInstance<TaskAdapter.TaskItem.Task>()
            .filter { it.task.urgent }
        if (urgentTasks.isNotEmpty()) {
            filteredList.add(TaskAdapter.TaskItem.Title(TaskType.ARGENT))
            filteredList.addAll(urgentTasks)
        }

        // Filter others tasks and add title
        val othersTasks = taskItems.filterIsInstance<TaskAdapter.TaskItem.Task>()
            .filter { !it.task.urgent }
        if (othersTasks.isNotEmpty()) {
            filteredList.add(TaskAdapter.TaskItem.Title(TaskType.OTHERS))
            filteredList.addAll(othersTasks)
        }

        return filteredList
    }

    private fun onFilterTasksClicked() {
        Log.d("main", "Filter tasks clicked!")
    }

    override fun onCompletedChanged(task: Task) {
        mainViewModel.onCompletedChanged(task)
        Log.d("main", "onCompletedChanged")
    }

    override fun onCardViewClicked(task: Task) {
        val action = AllTasksFragmentDirections.actionAllTasksFragmentToTaskDescriptionFragment(task.id)
        navController.navigate(action)
        Log.d("main", "onCardViewClicked")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("main", "onOptionsItemSelected")
        return when (item.itemId) {
            R.id.filterTasks -> {
                onFilterTasksClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}