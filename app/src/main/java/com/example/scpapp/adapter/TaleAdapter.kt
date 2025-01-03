package com.example.scpapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scpapp.data.tale.Tale
import com.example.scpapp.databinding.ItemTaleBinding

class TaleAdapter(private val onItemClick: (Tale) -> Unit) :
    ListAdapter<Tale, TaleAdapter.TaleViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Tale>() {
        override fun areItemsTheSame(oldItem: Tale, newItem: Tale): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Tale, newItem: Tale): Boolean {
            return oldItem.title == newItem.title && oldItem.rating == newItem.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaleViewHolder {
        val binding = ItemTaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaleViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: TaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaleViewHolder(
        private val binding: ItemTaleBinding,
        private val onItemClick: (Tale) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tale: Tale) {
            // Bind the Tale title
            binding.textViewTaleTitle.text = tale.title

            // Bind the Tale rating
            binding.textViewTaleRating.text = tale.rating.toString()

            // Format the SCP IDs as a comma-separated string
            val scpIdsString = tale.scpId.joinToString(", ")

            // Bind the SCP IDs to the TextView
            binding.textViewSCPIds.text = "Associated SCPs: ${scpIdsString.uppercase()}"

            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(tale)
            }
        }
    }
}
