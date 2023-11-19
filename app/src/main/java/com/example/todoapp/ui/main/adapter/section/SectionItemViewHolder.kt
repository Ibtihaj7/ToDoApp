package com.example.todoapp.ui.main.adapter.section

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CustomeSectionBinding
import com.example.todoapp.model.TaskSection
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.PostDetailListener
import com.example.todoapp.ui.main.adapter.task.TaskItemAdapter

class SectionItemViewHolder(
    private val binding: CustomeSectionBinding,
    private val completedChangeListener: CompletedChangeListener,
    private val postDetailListener : PostDetailListener
) : RecyclerView.ViewHolder(binding.root) {
    private val taskItemAdapter = TaskItemAdapter(completedChangeListener,postDetailListener)

    fun bind(taskSection: TaskSection) {
        binding.section = taskSection

        binding.sectionRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.sectionRv.adapter = taskItemAdapter

        taskItemAdapter.updateData(taskSection.getTasks())
    }
}