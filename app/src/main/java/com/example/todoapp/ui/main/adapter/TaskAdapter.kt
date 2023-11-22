package com.example.todoapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CustomeTaskBinding
import com.example.todoapp.databinding.CustomeTasksTitleBinding
import com.example.todoapp.model.TaskType
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.PostDetailListener

class TaskAdapter(
    private val completedChangeListener: CompletedChangeListener,
    private val postDetailListener: PostDetailListener
) : ListAdapter<TaskAdapter.TaskItem, RecyclerView.ViewHolder>(DiffCallback()) {
    fun updateData(newTasks: List<TaskItem>) {
        submitList(newTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TASKS_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomeTaskBinding.inflate(layoutInflater, parent, false)
                TaskViewHolder(binding, completedChangeListener, postDetailListener)
            }
            TITLES_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomeTasksTitleBinding.inflate(layoutInflater, parent, false)
                TitleViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                val taskItem = getItem(position) as TaskItem.Task
                holder.bind(taskItem.task)
            }
            is TitleViewHolder -> {
                val taskItem = getItem(position) as TaskItem.Title
                holder.bind(taskItem.title)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskItem.Task -> TASKS_VIEW
            is TaskItem.Title -> TITLES_VIEW
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return when {
                oldItem is TaskItem.Task && newItem is TaskItem.Task ->
                    oldItem.task.id == newItem.task.id
                oldItem is TaskItem.Title && newItem is TaskItem.Title ->
                    oldItem.title == newItem.title
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class TaskItem {
        data class Task(val task: com.example.todoapp.model.Task) : TaskItem()
        data class Title(val title: TaskType) : TaskItem()
    }

    companion object {
        const val TASKS_VIEW = 1
        const val TITLES_VIEW = 2
    }
}
