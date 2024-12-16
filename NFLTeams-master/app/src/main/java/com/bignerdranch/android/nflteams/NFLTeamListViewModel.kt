package com.bignerdranch.android.nflteams

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
private const val TAG = "TeamListVM"

class NFLTeamListViewModel : ViewModel() {

    private val teamRepo : TeamRepo = TeamRepo.get()
    private val _teamsFlow: MutableStateFlow<List<NFLTeam>> = MutableStateFlow(emptyList())
    val teamsFlow : StateFlow<List<NFLTeam>>
        get() = _teamsFlow.asStateFlow()

    init {
        Log.d(TAG, "init started")
        viewModelScope.launch {
            teamRepo.fetchTeams().collect{
                    teams -> _teamsFlow.value = teams
            }
        }
    }





    val teams = listOf(
        NFLTeam(
            UUID.randomUUID() ,
            "Chicago Bears",
            "bears.png",
            "NFC",
            "NFC North",
            "Soldier Field",
            41.8625,
            -87.616667
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Cincinnati Bengals",
            "bengals.png",
            "AFC",
            "AFC North",
            "Paul Brown Stadium",
            39.095444,
            -84.516039
        ),
        NFLTeam(
            UUID.randomUUID() ,
            "Buffalo Bills",
            "bills.png",
            "AFC",
            "AFC East",
            "Bills Stadium",
            42.773611,
            -78.786944
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Denver Broncos",
            "broncos.png",
            "AFC",
            "AFC West",
            "Empower Field at Mile High",
            39.743889,
            -105.02
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Cleveland Browns",
            "browns.png",
            "AFC",
            "AFC North",
            "FirstEnergy Stadium",
            41.506111,
            -81.699444
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Tampa Bay Buccaneers",
            "buccaneers.png",
            "NFC",
            "NFC South",
            "Raymond James Stadium",
            27.975833,
            -82.503333
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Arizona Cardinals",
            "cardinals.png",
            "NFC",
            "NFC West",
            "State Farm Stadium",
            33.5275,
            -112.2625
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Los Angeles Chargers",
            "chargers.png",
            "AFC",
            "AFC West",
            "SoFi Stadium",
            33.9533849,
            -118.3409007
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Kansas City Chiefs",
            "chiefs.png",
            "AFC",
            "AFC West",
            "Arrowhead Stadium",
            39.0489391,
            -94.4861044
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Indianapolis Colts",
            "colts.png",
            "AFC",
            "AFC South",
            "Lucas Oil Stadium",
            39.760056,
            -86.163806
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Dallas Cowboys",
            "cowboys.png",
            "NFC",
            "NFC East",
            "AT&T Stadium",
            32.747778,
            -97.092778
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Miami Dolphins",
            "dolphins.png",
            "AFC",
            "AFC East",
            "Hard Rock Stadium",
            25.958056,
            -80.238889
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Philadelphia Eagles",
            "eagles.png",
            "NFC",
            "NFC East",
            "Lincoln Financial Field",
            39.900833,
            -75.1675
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Atlanta Falcons",
            "falcons.png",
            "NFC",
            "NFC South",
            "Mercedes-Benz Stadium",
            33.755556,
            -84.400833
        ),
        NFLTeam(
            UUID.randomUUID(),
            "New York Giants",
            "giants.png",
            "NFC",
            "NFC East",
            "Met Life Stadium",
            40.813611,
            -74.074444
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Jacksonville Jaguars",
            "jaguars.png",
            "AFC",
            "AFC South",
            "TIAA Bank Field",
            30.323889,
            -81.6375
        ),
        NFLTeam(
            UUID.randomUUID(),
            "New York Jets",
            "jets.png",
            "AFC",
            "AFC East",
            "Met Life Stadium",
            40.813611,
            -74.074444
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Detroit Lions",
            "lions.png",
            "NFC",
            "NFC North",
            "Ford Field",
            42.34,
            -83.045556
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Green Bay Packers",
            "packers.png",
            "NFC",
            "NFC North",
            "Lambeau Field",
            44.501389,
            -88.062222
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Carolina Panthers",
            "panthers.png",
            "NFC",
            "NFC South",
            "Bank of America Stadium",
            35.225833,
            -80.852778
        ),
        NFLTeam(
            UUID.randomUUID(),
            "New England Patriots",
            "patriots.png",
            "AFC",
            "AFC East",
            "Gillette Stadium",
            42.090944,
            -71.264344
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Las Vegas Raiders",
            "raiders.png",
            "AFC",
            "AFC West",
            "Allegiant Stadium",
            36.090833,
            -115.183611
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Los Angeles Rams",
            "rams.png",
            "NFC",
            "NFC West",
            "SoFi Stadium",
            33.95345,
            -118.3392
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Baltimore Ravens",
            "ravens.png",
            "AFC",
            "AFC North",
            "M&T Bank Stadium",
            39.2779876,
            -76.6248931
        ),
        NFLTeam(
            UUID.randomUUID(),
            "New Orleans Saints",
            "saints.png",
            "NFC",
            "NFC South",
            "Mercedes-Benz Superdome",
            29.951061,
            -90.0834329
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Seattle Seahawks",
            "seahawks.png",
            "NFC",
            "NFC West",
            "CenturyLink Field",
            47.5951518,
            -122.3338281
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Pittsburgh Steelers",
            "steelers.png",
            "AFC",
            "AFC North",
            "Heinz Field",
            40.446667,
            -80.015833
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Houston Texans",
            "texans.png",
            "AFC",
            "AFC South",
            "NRG Stadium",
            29.684722,
            -95.410833
        ),
        NFLTeam(
            UUID.randomUUID(),
            "San Francisco 49ers",
            "the49ers.png",
            "NFC",
            "NFC West",
            "Levi's Stadium",
            37.403,
            121.97
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Tennessee Titans",
            "titans.png",
            "AFC",
            "AFC South",
            "Nissan Stadium",
            36.166251,
            -86.771422
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Minnesota Vikings",
            "vikings.png",
            "NFC",
            "NFC North",
            "Mall of America Field",
            44.973889,
            -93.258056
        ),
        NFLTeam(
            UUID.randomUUID(),
            "Washington Football Team",
            "washington.png",
            "NFC",
            "NFC East",
            "FedExField",
            38.907778,
            -76.864444
        )
    )
}