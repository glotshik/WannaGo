package com.bignerdranch.android.nflteams
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.nflteams.databinding.FragmentTeamListBinding
import kotlinx.coroutines.launch


private const val TAG = "NFLTeamListFragment"

class NFLTeamListFragment : Fragment() {
    private var _binding: FragmentTeamListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val nflteamlistviewmodel : NFLTeamListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total teams: ${nflteamlistviewmodel.teams.size}")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamListBinding.inflate(inflater, container, false)
        binding.teamRecyclerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                nflteamlistviewmodel.teamsFlow.collect{
                        teams ->
                    binding.teamRecyclerView.adapter =
                        NFLTeamAdapter(teams) { teamID ->
                            findNavController().navigate(
                                NFLTeamListFragmentDirections.showTeamDetail(teamID)
                            )
                        }

                }



            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
