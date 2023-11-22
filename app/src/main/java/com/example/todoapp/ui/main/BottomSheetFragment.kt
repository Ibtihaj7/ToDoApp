package com.example.todoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentButtomSheetBinding
import com.example.todoapp.utils.Constant.DATASTORE_NAME
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentButtomSheetBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentButtomSheetBinding.inflate(inflater)

        setUpStatus()
        setUpcomingClick()
        setUpcomingClicked()
        setAllTasksClicked()

        return binding.root
    }

    private fun setUpStatus() = lifecycleScope.launch(Dispatchers.Main) {
        val preferences = requireContext().datastore.data.first()

        val upcomingChecked = preferences[UPCOMING_KEY] ?: false
        val pastDueChecked = preferences[PAST_DUE_KEY] ?: false
        val allTasksChecked = preferences[ALL_TASKS_KEY] ?: false

        withContext(Dispatchers.Main) {
            if (upcomingChecked) binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)

            if (pastDueChecked) binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)

            if (allTasksChecked) binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
        }
    }

    private fun setAllTasksClicked() {
        binding.clearFilter.setOnClickListener {
            mainViewModel.initData()
            lifecycleScope.launch {
                saveFilterStatus(pastDue = false, upcoming = false,allTasks= true)
            }
            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_green_checked)
        }
    }

    private fun setUpcomingClicked() {
        binding.pastDue.setOnClickListener {
            mainViewModel.getTasksWithDueDatePassed()
            lifecycleScope.launch {
                saveFilterStatus(pastDue = true, upcoming = false, allTasks= false)
            }
            binding.upcomingIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_green_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private fun setUpcomingClick() {
        binding.upcoming.setOnClickListener {
            mainViewModel.getTasksWithDueDateUpcoming()
            lifecycleScope.launch {
                saveFilterStatus(pastDue = false, upcoming = true,allTasks= false)
            }
            binding.upcomingIcon.setImageResource(R.drawable.ic_green_checked)
            binding.pastDueIcon.setImageResource(R.drawable.ic_gray_checked)
            binding.clearFilterIcon.setImageResource(R.drawable.ic_gray_checked)
        }
    }

    private suspend fun saveFilterStatus(pastDue: Boolean, upcoming: Boolean,allTasks:Boolean) {
        requireContext().datastore.edit { preferences ->
            preferences[UPCOMING_KEY] = upcoming
            preferences[PAST_DUE_KEY] = pastDue
            preferences[ALL_TASKS_KEY] = allTasks
        }
    }

    companion object {
        val UPCOMING_KEY = booleanPreferencesKey("upcoming")
        val PAST_DUE_KEY = booleanPreferencesKey("pastDue")
        val ALL_TASKS_KEY = booleanPreferencesKey("allTasks")
    }
}