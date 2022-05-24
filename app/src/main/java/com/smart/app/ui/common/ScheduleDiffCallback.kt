package com.smart.app.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.smart.app.model.Schedule

class ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.namelabel == newItem.namelabel
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }

}