package com.example.todoapp.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CustomeTasksTitleBinding
import com.example.todoapp.model.TaskType

class TitleViewHolder(
    private val binding: CustomeTasksTitleBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(title: TaskType) {
        binding.title.text = title.displayName
    }
}