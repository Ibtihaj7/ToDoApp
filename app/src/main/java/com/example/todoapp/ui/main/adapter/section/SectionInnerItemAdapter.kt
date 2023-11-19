package com.example.todoapp.ui.main.adapter.section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.databinding.CustomeSectionBinding
import com.example.todoapp.model.TaskSection
import com.example.todoapp.ui.main.CompletedChangeListener
import com.example.todoapp.ui.main.PostDetailListener

class SectionInnerItemAdapter(
    private val completedChangeListener: CompletedChangeListener,
    private val postDetailListener : PostDetailListener
) : ListAdapter<TaskSection, SectionItemViewHolder>(DiffCallback()) {
    fun updateData(newPosts: List<TaskSection>) {
        submitList(newPosts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CustomeSectionBinding.inflate(layoutInflater, parent, false)
        return SectionItemViewHolder(binding,completedChangeListener,postDetailListener)
    }

    override fun onBindViewHolder(holder: SectionItemViewHolder, position: Int) {
        val currentPost = getItem(position)
        holder.bind(currentPost)
    }

    class DiffCallback:DiffUtil.ItemCallback<TaskSection>(){
        override fun areItemsTheSame(oldItem: TaskSection, newItem: TaskSection) =
            oldItem.getTitle() == newItem.getTitle()

        override fun areContentsTheSame(oldItem: TaskSection, newItem: TaskSection) =
            oldItem == newItem
    }
}