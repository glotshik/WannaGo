package com.example.wannago

import android.app.Application
import com.google.firebase.FirebaseApp

class WannaGoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}