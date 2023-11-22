package com.example.todoapp.ui.main.addnewtask

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddNewTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentAddNewTaskBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewTaskBinding.inflate(inflater, container, false)
        navController = findNavController()
        context = requireContext()

        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.datePickerButton.setOnClickListener { openDatePicker() }
        binding.addTaskButton.setOnClickListener { addNewTask() }

        val toolbar: Toolbar = binding.toolbar
        setupToolbar(toolbar)
    }

    private fun addNewTask() {
        val title = binding.titleEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val isUrgent = binding.urgentSwitch.isChecked
        val selectedDateString = binding.datePickerButton.text.toString().trim()

        val isValid = validateInput(title, description, selectedDateString)

        if (isValid) {
            val selectedDate = selectedDateString.toDate()
            val newTask = Task(0,title,description,selectedDate,isUrgent)
            mainViewModel.addNewTask(newTask)
            showTaskAddedSnackBar()
            navigateToAllTasksFragment()
        } else {
            handleValidationErrors(title, description, selectedDateString)
        }
    }
    private fun String.toDate(): Date {
        val format = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
        return try {
            format.parse(this) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    private fun validateInput(title: String, description: String, selectedDate: String) =
        !(title.isEmpty() || description.isEmpty() || selectedDate.isEmpty())

    private fun showTaskAddedSnackBar() {
        Snackbar.make(
            binding.root,
            getTaskAddedSuccessfully(context),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleValidationErrors(
        title: String,
        description: String,
        selectedDate: String
    ) {
        binding.titleInputLayout.error = if (title.isEmpty()) getTitleRequiredError(context) else null
        binding.descriptionInputLayout.error =
            if (description.isEmpty()) getDescriptionRequiredError(context) else null
        binding.dueDateErrorTextView.visibility = if (selectedDate.isEmpty()) View.VISIBLE else View.GONE
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

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "${Month.values()[month - 1].abbreviation} $day $year"
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = makeDateString(dayOfMonth, month + 1, year)
        binding.datePickerButton.text = selectedDate
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
        fun getTitleRequiredError(context: Context) =  context.getString(R.string.error_title_required)
        fun getDescriptionRequiredError(context: Context) = context.getString(R.string.error_description_required)
        fun getTaskAddedSuccessfully(context: Context) = context.getString(R.string.successfully_task_added)
    }
}
