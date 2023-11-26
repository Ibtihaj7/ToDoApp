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

        setUpStatus()
        setUpcomingClick()
        setPastDueClicked()
        setAllTasksClicked()

        return binding.root
    }

    private fun setUpStatus() = lifecycleScope.launch {
        if (viewModel.isUpcomingChecked()) binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)
        if (viewModel.isPastDueChecked()) binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)
        if (viewModel.isAllTasksChecked()) binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
    }

    private fun setUpcomingClick() {
        binding.upcoming.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = false, upcoming = true,allTasks= false)
            mainViewModel.initData()

            binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private fun setPastDueClicked() {
        binding.pastDue.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = true, upcoming = false, allTasks= false)
            mainViewModel.initData()

            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private fun setAllTasksClicked() {
        binding.clearFilter.setOnClickListener {
            mainViewModel.saveFilterStatus(pastDue = false, upcoming = false,allTasks= true)
            mainViewModel.initData()

            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
        }
    }
}