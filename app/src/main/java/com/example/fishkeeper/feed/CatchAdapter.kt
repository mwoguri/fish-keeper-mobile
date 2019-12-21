package com.example.fishkeeper.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishkeeper.databinding.ListItemCatchBinding
import com.example.fishkeeper.network.CatchResponse

class CatchAdapter : ListAdapter<CatchResponse, CatchAdapter.ViewHolder>(CatchDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catch = getItem(position)
        holder.bind(catch)
    }

    class CatchDiffCallback : DiffUtil.ItemCallback<CatchResponse>() {
        override fun areItemsTheSame(oldItem: CatchResponse, newItem: CatchResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatchResponse, newItem: CatchResponse): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder private constructor(val binding: ListItemCatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemCatchBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(catch: CatchResponse) {
            binding.myCatch = catch
//            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}