package com.example.wannago

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wannago.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.car.ui.toolbar.MenuItem
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Set up ActionBarDrawerToggle
        actionBarToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        // Enable the up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set a default fragment when the app starts
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlacesFragment())
                .commit()
        }

        // Navigation item selection
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_places -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PlacesFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_about -> {
                    // TODO: Create and show About fragment
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    // Handle menu item selections
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return if (actionBarToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}