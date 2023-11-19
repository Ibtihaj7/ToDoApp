package com.example.todoapp.ui.main.taskdescription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskDescriptionBinding
import com.example.todoapp.model.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class TaskDescriptionFragment : Fragment() {
    private val viewModel:TaskDescriptionViewModel by viewModels()
    private lateinit var binding:FragmentTaskDescriptionBinding
    private val args: TaskDescriptionFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDescriptionBinding.inflate(inflater)


        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            val action = TaskDescriptionFragmentDirections.actionTaskDescriptionFragmentToAllTasksFragment()
            findNavController().navigate(action)
        }


        val task = Task(1,"solve homework","mmfmfmfmfkkfkfkfkfkskfkdfkdf", Date(),true,false)
        binding.task = task
        return binding.root
    }

}