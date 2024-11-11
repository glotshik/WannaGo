package com.example.nflteams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nflteams.NFLTeamAdapter
import com.example.nflteams.NFLTeamListViewModel
import com.example.nflteams.databinding.FragmentTeamListBinding

class NFLTeamListFragment : Fragment() {

    private var _binding: FragmentTeamListBinding? = null
    private val binding get() = _binding!!

    private val nflTeamListViewModel: NFLTeamListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamListBinding.inflate(inflater, container, false)
        binding.nflteamRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val teams = nflTeamListViewModel.teams
        binding.nflteamRecyclerView.adapter = NFLTeamAdapter(teams)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}