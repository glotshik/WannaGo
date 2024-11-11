package com.example.nflteams
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nflteams.NFLTeam
import com.example.nflteams.databinding.ListItemTeamBinding

class NFLTeamAdapter(private val teams: List<NFLTeam>) :
    RecyclerView.Adapter<NFLTeamAdapter.NFLTeamHolder>() {

    inner class NFLTeamHolder(private val binding: ListItemTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(team: NFLTeam) {
            binding.listTeamName.text = team.teamName
            binding.listTeamStadium.text = team.stadium
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NFLTeamHolder {
        val binding = ListItemTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NFLTeamHolder(binding)
    }

    override fun onBindViewHolder(holder: NFLTeamHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount() = teams.size
}