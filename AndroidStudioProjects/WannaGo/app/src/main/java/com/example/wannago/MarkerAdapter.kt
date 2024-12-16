package com.example.wannago

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wannago.databinding.ItemMakerBinding

class MarkerAdapter(
    private val onDeleteClick: (MarkerLocation) -> Unit,
    private val onItemClick: (MarkerLocation) -> Unit
) : ListAdapter<MarkerLocation, MarkerAdapter.MarkerViewHolder>(MarkerDiffCallback()) {

    inner class MarkerViewHolder(
        private val binding: ItemMakerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: MarkerLocation) {
            binding.apply {
                root.setOnClickListener { onItemClick(marker) }
                tvAddress.text = marker.address
                tvLatitude.text = "Latitude: ${String.format("%.6f", marker.latitude)}"
                tvLongitude.text = "Longitude: ${String.format("%.6f", marker.longitude)}"
                btnDelete.setOnClickListener {
                    onDeleteClick(marker)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        return MarkerViewHolder(
            ItemMakerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class MarkerDiffCallback : DiffUtil.ItemCallback<MarkerLocation>() {
    override fun areItemsTheSame(oldItem: MarkerLocation, newItem: MarkerLocation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarkerLocation, newItem: MarkerLocation): Boolean {
        return oldItem == newItem
    }
}