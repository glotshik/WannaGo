package com.bignerdranch.android.nflteams

import java.util.UUID

data class NFLTeam(
    val teamID: UUID ,
    val teamName:String,
    val logoFile: String,
    val conference: String,
    val division: String,
    val stadium: String,
    val latitude: Double,
    val longitude: Double


            )
