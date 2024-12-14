package com.example.hyrule.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyrule.databinding.CharacterListItemBinding
import com.example.hyrule.databinding.FragmentCharactersBinding

class CharacterAdapter(val characters: List<Hyrulers>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(
        private val binding: CharacterListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hyrulers: Hyrulers) {
            binding.characterNameText.text = hyrulers.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CharacterListItemBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val hyrulers = characters[position]
        holder.bind(hyrulers)
    }

    override fun getItemId(position: Int): Long = position.toLong()
}