package com.example.nflteams
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nflteams.R
import com.example.nflteams.NFLTeamListFragment



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         // Always replace the fragment, even if savedInstanceState is not null
        val fragment = NFLTeamListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
