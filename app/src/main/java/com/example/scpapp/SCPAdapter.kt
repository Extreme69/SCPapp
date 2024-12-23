package com.example.scpapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scpapp.databinding.ItemScpBinding

class SCPAdapter : ListAdapter<SCP, SCPAdapter.SCPViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SCP>() {
        override fun areItemsTheSame(oldItem: SCP, newItem: SCP): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SCP, newItem: SCP): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SCPViewHolder {
        val binding = ItemScpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SCPViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SCPViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SCPViewHolder(private val binding: ItemScpBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scp: SCP) {
            binding.textViewSCPTitle.text = scp.title
            binding.textViewSCPDescription.text = scp.description
            // Load image using a library like Glide/Picasso
        }
    }
}
