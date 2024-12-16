package com.bignerdranch.android.nflteams

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
private const val TAG = "TeamDetailVM"
class NFLTeamDetailViewModel(teamID: UUID) : ViewModel()   {
    private val teamRepo = TeamRepo.get()
    private val _team: MutableStateFlow<NFLTeam?> = MutableStateFlow(null)
    val team: StateFlow<NFLTeam?> = _team.asStateFlow()


    init{
        viewModelScope.launch{
            _team.value = teamRepo.fetchById(teamID)
            Log.d(TAG , "Logging out team  " + _team.value )
        }
    }

    fun updateTeam(onUpdate: (NFLTeam) -> NFLTeam){
        _team.update{ oldTeam ->
            oldTeam?.let{onUpdate(it)}

        }
    }
    override fun onCleared(){
        super.onCleared()
        team.value?.let{
            teamRepo.updateTeam(it)
        }
    }
}

class NFLTeamDetailVMFactory(private val teamID: UUID): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        return NFLTeamDetailViewModel(teamID) as T
    }

}