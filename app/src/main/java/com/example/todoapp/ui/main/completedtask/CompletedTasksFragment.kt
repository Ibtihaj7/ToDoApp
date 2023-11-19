package com.example.todoapp.ui.main.completedtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCompletedTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedTasksFragment : Fragment() {
    private lateinit var binding:FragmentCompletedTasksBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedTasksBinding.inflate(inflater)
        toolbar = binding.toolbar

//        (AppCompatActivity) as


        return binding.root
    }

}