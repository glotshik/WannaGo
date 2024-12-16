package com.bignerdranch.android.nflteams

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nflteams.databinding.ListItemTeamBinding
import java.util.UUID

class TeamHolder(
    private val binding: ListItemTeamBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(team : NFLTeam , onTeamClicked: (teamID: UUID) -> Unit  ) {
        binding.teamTitle.text = team.teamName
        binding.teamDivision.text = team.division
        binding.teamConference.text = team.conference
        binding.teamId.text = team.teamID.toString()
        binding.teamLatitude.text = team.latitude.toString()
        binding.teamLongitude.text = team.longitude.toString()
        binding.teamStadium.text = team.stadium



        binding.root.setOnClickListener {
            onTeamClicked(team.teamID)
        }
    }
}


class NFLTeamAdapter(
    private val teams : List<NFLTeam> ,
    private val onTeamClicked: (teamID: UUID) -> Unit
) : RecyclerView.Adapter<TeamHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : TeamHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTeamBinding.inflate(inflater, parent, false)
        return TeamHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        val team = teams[position]
        holder.bind(team , onTeamClicked)
    }

    override fun getItemCount() = teams.size
}