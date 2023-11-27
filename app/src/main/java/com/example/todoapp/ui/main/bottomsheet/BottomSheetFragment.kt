package com.example.todoapp.ui.main.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentButtomSheetBinding
import com.example.todoapp.ui.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentButtomSheetBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel : BottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentButtomSheetBinding.inflate(inflater)

        observeUiStatus()
        setUpcomingClick()
        setPastDueClicked()
        setAllTasksClicked()

        return binding.root
    }

    private fun observeUiStatus() = lifecycleScope.launch {
        viewModel.upcomingStatus.observe(viewLifecycleOwner){
            val img = if(it) R.drawable.ic_green_checked else R.drawable.ic_gray_checked
            binding.upcomingIcon.setImageResource(img)
        }
        viewModel.allTasksStatus.observe(viewLifecycleOwner){
            val img = if(it) R.drawable.ic_green_checked else R.drawable.ic_gray_checked
            binding.clearFilterIcon.setImageResource(img)
        }
        viewModel.pastDueStatus.observe(viewLifecycleOwner){
            val img = if(it) R.drawable.ic_green_checked else R.drawable.ic_gray_checked
            binding.pastDueIcon.setImageResource(img)
        }
    }

    private fun setUpcomingClick() {
        binding.upcoming.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = false, upcoming = true,allTasks= false)
            viewModel.checkPreferences()
        }
    }

    private fun setPastDueClicked() {
        binding.pastDue.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = true, upcoming = false, allTasks= false)
            viewModel.checkPreferences()
        }
    }

    private fun setAllTasksClicked() {
        binding.clearFilter.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = false, upcoming = false,allTasks= true)
            viewModel.checkPreferences()
        }
    }
}