package com.bignerdranch.android.nflteams

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.nflteams.databinding.FragmentNflTeamDetailBinding
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.launch
import java.util.UUID
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class NFLTeamDetailFragment : Fragment() , OnMapReadyCallback {
    private var _binding :FragmentNflTeamDetailBinding? = null
    private lateinit var nflteam : NFLTeam
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access Binding because it is null"
        }




    private val args: NFLTeamDetailFragmentArgs by navArgs()
    private val teamDetailViewModel: NFLTeamDetailViewModel by viewModels {
        NFLTeamDetailVMFactory(args.teamID)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nflteam = NFLTeam (
        teamName = "Colts" ,
        logoFile = "colts.png",
        conference = "AFC" ,
        division = "AFC" ,
        stadium = "Lucas Oil Stadium" ,
        teamID = UUID.randomUUID() ,
        latitude = 20.22,
        longitude = 10.11
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentNflTeamDetailBinding.inflate(layoutInflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.apply {

            teamName.doOnTextChanged { text, _, _, _ ->
                teamDetailViewModel.updateTeam { oldTeam ->
                    oldTeam.copy(teamName = text.toString())
                }

            }
            teamStadium.apply {
                isEnabled = false
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamDetailViewModel.team.collect { team ->
                    team?.let { updateUi(it) }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateUi(team: NFLTeam) {
        binding.apply {
            if (teamName.text.toString() != team.teamName) {
                teamName.text = team.teamName
            }
            teamStadium.text = team.stadium
            teamDivision.text = team.division
            displayTeamName.text = team.teamName


        }
    }
private lateinit var map: GoogleMap
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        val coords = LatLng(teamDetailViewModel.team.value?.latitude ?: 0.0, teamDetailViewModel.team.value?.longitude ?: 0.0 )
        Log.d("TeamLatitude", "${teamDetailViewModel.team.value?.latitude ?: 0.0}")
        Log.d("TeamLongitude", "${teamDetailViewModel.team.value?.longitude ?: 0.0}")


        map.addMarker(
            MarkerOptions()
                .position(coords)
                .visible(true)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords , 15f))
        binding.mapView.onResume()

    }
}
