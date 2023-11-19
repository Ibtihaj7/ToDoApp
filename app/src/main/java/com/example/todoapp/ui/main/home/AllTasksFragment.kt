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
import com.example.todoapp.model.TaskSection
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.MainViewModel
import com.example.todoapp.ui.main.PostDetailListener
import com.example.todoapp.ui.main.adapter.section.SectionInnerItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AllTasksFragment : Fragment(), CompletedChangeListener, PostDetailListener {

    private val viewModel: AllTasksViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAllTasksBinding
    private val taskSection: MutableList<TaskSection> = mutableListOf<TaskSection>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllTasksBinding.inflate(inflater)
        navController = findNavController()
        setHasOptionsMenu(true)

        initUI()
        initData()
        observeTasksList()

        return binding.root
    }

    private fun initUI() {
        val sectionAdapter = SectionInnerItemAdapter(this, this)
        sectionAdapter.updateData(taskSection)
        binding.tasksRv.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRv.adapter = sectionAdapter

        binding.addTaskFloatingButton.setOnClickListener {
            val action = AllTasksFragmentDirections.actionAllTasksFragmentToAddNewTaskFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeTasksList() {

    }

    private fun initData(){
        val task1 = Task(0,"ibtihaj","adham", Date(),false,isCompleted =false)
        val task2 = Task(1,"abrar","hannon", Date(),true,isCompleted =false)
        val task3 = Task(2,"aya","ahmad", Date(),false,isCompleted =false)
        val task4 = Task(3,"omar","yaseen", Date(),true,isCompleted =false)
        val task5 = Task(4,"ali","adam", Date(),true,isCompleted =false)
        val task6 = Task(5,"zaid","amer", Date(),false,isCompleted =false)
        val task7 = Task(6,"weam","ali", Date(),true,isCompleted =true)
        val task8 = Task(7,"amaar","alyaser", Date(),false,isCompleted =true)
        val task9 = Task(8,"hummam","ahmad", Date(),false,isCompleted =true)
        val task10 = Task(9,"mahmoud","hasan", Date(),false, isCompleted = true)

        val tasks:ArrayList<Task> = arrayListOf()
        tasks.add(task1)
        tasks.add(task2)
        tasks.add(task5)

        val tasks2:ArrayList<Task> = arrayListOf()
        tasks2.add(task6)
        tasks2.add(task8)
        tasks2.add(task9)


        taskSection.add(TaskSection("urgent",tasks))
        taskSection.add(TaskSection("others",tasks2))

        Log.d("main",taskSection.toString())
    }

    private fun onFilterTasksClicked() {
        Log.d("main", "Filter tasks clicked!")
    }

    override fun onCompletedChanged(task: Task) {
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
        when (item.itemId) {
            R.id.filterTasks -> {
                onFilterTasksClicked()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
