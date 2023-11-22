package com.example.todoapp.ui.main.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentButtomSheetBinding
import com.example.todoapp.ui.main.MainViewModel
import com.example.todoapp.utils.Constant.ALL_TASKS_KEY
import com.example.todoapp.utils.Constant.DATASTORE_NAME
import com.example.todoapp.utils.Constant.PAST_DUE_KEY
import com.example.todoapp.utils.Constant.UPCOMING_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentButtomSheetBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel:BottomSheetViewModel by viewModels()
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
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

    private fun setUpStatus() = lifecycleScope.launch(Dispatchers.Main) {
        try {
            val preferences = requireContext().datastore.data.first()

            val upcomingChecked = preferences[UPCOMING_KEY] ?: false
            val pastDueChecked = preferences[PAST_DUE_KEY] ?: false
            val allTasksChecked = preferences[ALL_TASKS_KEY] ?: false

            withContext(Dispatchers.Main) {
                if (upcomingChecked) binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)
                if (pastDueChecked) binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)
                if (allTasksChecked) binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setAllTasksClicked() {
        binding.clearFilter.setOnClickListener {
            mainViewModel.initData()

            saveFilterStatus(pastDue = false, upcoming = false,allTasks= true)

            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
        }
    }

    private fun setPastDueClicked() {
        binding.pastDue.setOnClickListener {
            mainViewModel.getTasksWithDueDatePassed()

            saveFilterStatus(pastDue = true, upcoming = false, allTasks= false)

            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private fun setUpcomingClick() {
        binding.upcoming.setOnClickListener {
            mainViewModel.getTasksWithDueDateUpcoming()

            saveFilterStatus(pastDue = false, upcoming = true,allTasks= false)

            binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private fun saveFilterStatus(pastDue: Boolean, upcoming: Boolean,allTasks:Boolean)=lifecycleScope.launch(Dispatchers.Main) {
        requireContext().datastore.edit { preferences ->
            preferences[UPCOMING_KEY] = upcoming
            preferences[PAST_DUE_KEY] = pastDue
            preferences[ALL_TASKS_KEY] = allTasks
        }
    }
}