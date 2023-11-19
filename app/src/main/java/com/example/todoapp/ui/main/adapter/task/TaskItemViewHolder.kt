package com.example.todoapp.ui.main.adapter.task

import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.CustomeTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.PostDetailListener

class TaskItemViewHolder(
    private val binding: CustomeTaskBinding,
    private val completedChangeListener: CompletedChangeListener,
    private val postDetailListener : PostDetailListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task) {
        binding.task = task
        setupItemView(task)
        setupCompletedButton(task)
    }

    private fun setupItemView(task: Task){
        itemView.setOnClickListener {
            postDetailListener.onCardViewClicked(task)
        }
    }

    private fun setupCompletedButton(task: Task){
        val imgId = if(!task.isCompleted) R.drawable.ic_not_completed else R.drawable.ic_completed
        binding.iconImageView.setImageResource(imgId)

        binding.iconImageView.setOnClickListener {
            completedChangeListener.onCompletedChanged(task)
        }
    }
}
