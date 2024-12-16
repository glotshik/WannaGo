package com.example.placesdemo

import MarkerLocation
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.placesdemo.databinding.ItemMarkerBinding

class MarkerAdapter(private val markers: List<MarkerLocation>) :
    RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    inner class MarkerViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: MarkerLocation) {
            binding.tvLatitude.text = "Lat: ${marker.latitude}"
            binding.tvLongitude.text = "Lng: ${marker.longitude}"
            binding.tvAddress.text = marker.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = ItemMarkerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(markers[position])
    }

    override fun getItemCount() = markers.size
}