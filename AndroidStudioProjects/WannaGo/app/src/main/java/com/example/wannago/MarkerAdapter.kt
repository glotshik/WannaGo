package com.example.wannago

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wannago.databinding.ItemMakerBinding

class MarkerAdapter(
    private val markers: List<MarkerLocation>,
    private val onDeleteClick: (MarkerLocation) -> Unit
) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    inner class MarkerViewHolder(
        private val binding: ItemMakerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: MarkerLocation) {
            binding.tvAddress.text = marker.address
            binding.tvLatitude.text = "Lat: ${marker.latitude}"
            binding.tvLongitude.text = "Lon: ${marker.longitude}"

            binding.btnDelete.setOnClickListener {
                onDeleteClick(marker)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMakerBinding.inflate(inflater, parent, false)
        return MarkerViewHolder(binding)
    }

    override fun getItemCount(): Int = markers.size

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(markers[position])
    }
}


