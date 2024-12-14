package com.example.listr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listr.databinding.ListItemBinding

class ListrAdapter(val items: List<ListItem>) :
    RecyclerView.Adapter<ListrAdapter.ListrViewHolder>() {

    inner class ListrViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItem) {
            binding.itemNameText.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListrViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListrViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListrViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}