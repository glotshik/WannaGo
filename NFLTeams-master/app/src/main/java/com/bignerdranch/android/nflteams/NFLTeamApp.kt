package com.bignerdranch.android.nflteams

import android.app.Application

class NFLTeamApp : Application() {
override fun onCreate() {
    super.onCreate()
    TeamRepo.initialize()

    }
}