package com.example.todoapp.ui.main.addnewtask

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentAddNewTaskBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class AddNewTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddNewTaskBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var navController: NavController
    private val viewModel:AddNewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewTaskBinding.inflate(inflater, container, false)
        navController = findNavController()

        setupUI()
        return binding.root
    }

    private fun setupUI() {
        initDatePicker()
        binding.datePickerButton.setOnClickListener { openDatePicker() }
        binding.addTaskButton.setOnClickListener { addNewTask() }

        val toolbar: Toolbar = binding.toolbar
        setupToolbar(toolbar)
    }

    private fun addNewTask() {
        val titleEditText = binding.titleEditText
        val descriptionEditText = binding.descriptionEditText
        val urgentSwitch = binding.urgentSwitch
        val datePickerButton = binding.datePickerButton

        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val isUrgent = urgentSwitch.isChecked
        val selectedDate = datePickerButton.text.toString().trim()

        val isValid = viewModel.validateInput(title,description,selectedDate)

        if (isValid) {
            viewModel.addNewTask(title, description, isUrgent,selectedDate)
            showTaskAddedSnackBar()
            navigateToAllTasksFragment()
        } else {
            handleValidationErrors(title, description,selectedDate)
        }
    }
    private fun showTaskAddedSnackBar() {
        Snackbar.make(
            binding.root,
            SUCCESSFULLY_TASK_ADDED,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleValidationErrors(
        title: String,
        description: String,
        selectedDate: String
    ) {
        binding.titleInputLayout.error = if (title.isEmpty()) ERROR_TITLE_REQUIRED else null
        binding.descriptionInputLayout.error = if (description.isEmpty()) ERROR_DESCRIPTION_REQUIRED else null
        binding.dueDateErrorTextView.visibility = if(selectedDate.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            navigateToAllTasksFragment()
        }
    }

    private fun navigateToAllTasksFragment() {
        val action = AddNewTaskFragmentDirections.actionAddNewTaskFragmentToAllTasksFragment()
        navController.navigate(action)
    }

    private fun initDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val formattedDate = makeDateString(day, month + 1, year)
                binding.datePickerButton.text = formattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "${Month.values()[month - 1].abbreviation} $day $year"
    }

    private fun openDatePicker() {
        datePickerDialog.show()
    }

    enum class Month(val abbreviation: String) {
        JAN("JAN"),
        FEB("FEB"),
        MAR("MAR"),
        APR("APR"),
        MAY("MAY"),
        JUN("JUN"),
        JUL("JUL"),
        AUG("AUG"),
        SEP("SEP"),
        OCT("OCT"),
        NOV("NOV"),
        DEC("DEC")
    }

    companion object {
        const val ERROR_TITLE_REQUIRED = "Title is required"
        const val ERROR_DESCRIPTION_REQUIRED = "Description is required"
        const val SUCCESSFULLY_TASK_ADDED ="Task added successfully"
    }
}
