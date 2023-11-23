package com.example.todoapp.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
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
import com.example.todoapp.utils.Constant.ALL_TASKS_KEY
import com.example.todoapp.utils.Constant.PAST_DUE_KEY
import com.example.todoapp.utils.Constant.UPCOMING_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllTasksFragment : Fragment(), CompletedChangeListener, PostDetailListener {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAllTasksBinding
    private lateinit var tasksAdapter: TaskAdapter
    private var handler: Handler? = null

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllTasksBinding.inflate(inflater)
        navController = findNavController()

        initializeFiltersBasedOnPreferences()
        initializeViews()
        initializeAppBar()
        observeTasksList()

        return binding.root
    }

    private fun initializeFiltersBasedOnPreferences() = lifecycleScope.launch(Dispatchers.Main){
       try{
            val preferences = dataStore.data.first()

            val upcomingChecked = preferences[UPCOMING_KEY] ?: false
            val pastDueChecked = preferences[PAST_DUE_KEY] ?: false

            if(upcomingChecked) mainViewModel.getTasksWithDueDateUpcoming()
            if(pastDueChecked) mainViewModel.getTasksWithDueDatePassed()
       } catch (e: Exception) {
           e.printStackTrace()
           Log.d("main",e.message.toString())
       }
    }

    private fun initializeViews() {
        initializeAdapter()

        binding.addTaskFloatingButton.setOnClickListener {
            navigateToAddNewTaskFragment()
        }
    }

    private fun initializeAdapter() {
        tasksAdapter = TaskAdapter(this, this)
        binding.tasksRv.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRv.adapter = tasksAdapter
    }

    private fun observeTasksList() {
        mainViewModel.tasksList.observe(viewLifecycleOwner) { taskItems ->
            taskItems?.let {
                if (::tasksAdapter.isInitialized) {
                    tasksAdapter.updateData(filterTasks(taskItems))

                    val noResultsTextView = binding.noResultsTextView
                    noResultsTextView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    initializeAdapter()
                }
            }
        }
    }

    private fun filterTasks(taskItems: List<TaskAdapter.TaskItem>): List<TaskAdapter.TaskItem> {
        val filteredList = mutableListOf<TaskAdapter.TaskItem>()

        val urgentTasks = taskItems.filterIsInstance<TaskAdapter.TaskItem.Task>()
            .filter { it.task.urgent }
        if (urgentTasks.isNotEmpty()) {
            filteredList.add(TaskAdapter.TaskItem.Title(TaskType.ARGENT))
            filteredList.addAll(urgentTasks)
        }

        val othersTasks = taskItems.filterIsInstance<TaskAdapter.TaskItem.Task>()
            .filter { !it.task.urgent }
        if (othersTasks.isNotEmpty()) {
            filteredList.add(TaskAdapter.TaskItem.Title(TaskType.OTHERS))
            filteredList.addAll(othersTasks)
        }
        return filteredList
    }

    private fun navigateToAddNewTaskFragment() {
        val action = AllTasksFragmentDirections.actionAllTasksFragmentToAddNewTaskFragment()
        findNavController().navigate(action)
    }

    private fun initializeAppBar() {
        val menuHost: MenuHost = binding.toolbar

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filterTasks -> {
                        onFilterTasksClicked()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupSearchViewListener()
    }

    private fun setupSearchViewListener() {
        val searchItem = binding.toolbar.menu.findItem(R.id.searchTask)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler?.removeCallbacksAndMessages(null)
                handler = Handler(Looper.getMainLooper())
                handler?.postDelayed({ filterTasksBySearch(newText) }, FILTER_DELAY)
                return true
            }
        })
    }

    private fun filterTasksBySearch(query: String?) {
        mainViewModel.filterList(query)
        updateDataStorePreferences()
    }

    private fun updateDataStorePreferences() {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[UPCOMING_KEY] = false
                preferences[PAST_DUE_KEY] = false
                preferences[ALL_TASKS_KEY] = false
            }
        }
    }

    private fun onFilterTasksClicked() {
        val action = AllTasksFragmentDirections.actionAllTasksFragmentToButtomSheetFragment()
        navController.navigate(action)
    }

    override fun onCompletedChanged(task: Task) {
        mainViewModel.onCompletedChanged(task)
    }

    override fun onCardViewClicked(task: Task) {
        val action = AllTasksFragmentDirections.actionAllTasksFragmentToTaskDescriptionFragment(task.id)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val FILTER_DELAY: Long = 500
    }
}