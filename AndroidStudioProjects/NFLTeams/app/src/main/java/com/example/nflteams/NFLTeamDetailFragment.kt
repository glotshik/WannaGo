package com.example.nflteams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nflteams.databinding.FragmentTeamDetailBinding

class NFLTeamDetailFragment : Fragment() {
    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var team: NFLTeam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        team = NFLTeam(
            teamId = "NE",
            teamName = "New England Patriots",
            logoFile = "patriots_logo",
            conference = "AFC",
            division = "East",
            stadium = "Gillette Stadium",
            latitude = 42.0909,
            longitude = -71.2643
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        binding.apply {
            teamLogo.setImageResource(resources.getIdentifier(team.logoFile, "drawable", requireContext().packageName))
            teamName.text = team.teamName
            teamDivision.text = "${team.conference} ${team.division}"
            stadiumName.text = team.stadium
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}