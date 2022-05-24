package com.smart.app.ui.categorydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.app.databinding.ItemScheduleWeekBinding
import com.smart.app.model.Schedule
import com.smart.app.ui.common.ScheduleDiffCallback

class ScheduleAdapter :
    ListAdapter<Schedule, ScheduleAdapter.ScheduleItemViewHolder>(ScheduleDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItemViewHolder {
        val binding = ItemScheduleWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleItemViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: ScheduleItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ScheduleItemViewHolder(
        private val binding: ItemScheduleWeekBinding,
        private val viewType: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            binding.schedule = schedule
            binding.executePendingBindings()
        }

    }


}


/*
fun bind(category: Category) {
            binding.viewModel = viewModel
            binding.category = category
            binding.executePendingBindings()
        }
 */