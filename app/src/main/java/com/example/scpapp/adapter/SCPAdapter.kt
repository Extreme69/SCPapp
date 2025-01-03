package com.example.scpapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scpapp.data.scp.SCP
import com.example.scpapp.databinding.ItemScpBinding

class SCPAdapter(private val onItemClick: (SCP) -> Unit) :
    ListAdapter<SCP, SCPAdapter.SCPViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SCP>() {
        override fun areItemsTheSame(oldItem: SCP, newItem: SCP): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SCP, newItem: SCP): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SCPViewHolder {
        val binding = ItemScpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SCPViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SCPViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SCPViewHolder(
        private val binding: ItemScpBinding,
        private val onItemClick: (SCP) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(scp: SCP) {
            // Bind the SCP ID and Title (e.g., "SCP-173: The Statue")
            binding.textViewSCPTitle.text = "${scp.id}: ${scp.title}"

            // Bind the SCP Classification (e.g., "Keter")
            binding.textViewSCPClassification.text = scp.classification

            // Bind the SCP Rating (e.g., "93")
            binding.textViewSCPRating.text = scp.rating.toString()

            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(scp)
            }
        }
    }
}
