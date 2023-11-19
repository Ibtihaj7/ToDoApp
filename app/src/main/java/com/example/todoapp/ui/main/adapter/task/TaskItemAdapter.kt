package com.example.todoapp.ui.main.adapter.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.databinding.CustomeTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.PostDetailListener

class TaskItemAdapter(
    private val completedChangeListener: CompletedChangeListener,
    private val postDetailListener : PostDetailListener
) : ListAdapter<Task, TaskItemViewHolder>(DiffCallback()) {
    fun updateData(newPosts: List<Task>) {
        submitList(newPosts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CustomeTaskBinding.inflate(layoutInflater, parent, false)
        return TaskItemViewHolder(binding,completedChangeListener,postDetailListener)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    class DiffCallback: DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}
