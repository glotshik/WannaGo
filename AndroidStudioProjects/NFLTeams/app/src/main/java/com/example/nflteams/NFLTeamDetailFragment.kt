package com.example.nflteams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nflteams.databinding.FragmentTeamDetailBinding

private const val ARG_TEAM_ID = "team_id"

class NFLTeamDetailFragment : Fragment() {
    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var team: NFLTeam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val teamId = arguments?.getString(ARG_TEAM_ID) ?: ""
        // In a real app, you would get the team from your ViewModel using the teamId
        // For now, we'll just create a sample team
        team = NFLTeam(
            teamId = teamId,
            teamName = "New England Patriots", // This should come from your data source
            logoFile = "patriots_logo",
            conference = "AFC",
            division = "East",
            stadium = "Gillette Stadium",
            latitude = 42.0909,
            longitude = -71.2643
        )
    }

    companion object {
        fun newInstance(teamId: String): NFLTeamDetailFragment {
            val args = Bundle().apply {
                putString(ARG_TEAM_ID, teamId)
            }
            return NFLTeamDetailFragment().apply {
                arguments = args
            }
        }
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