package edu.elliott.hipsterbands

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import edu.elliott.hipsterbands.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate: View setup complete")

        binding.submitButton.setOnClickListener {
            Log.d("MainActivity", "Submit button clicked")
            generateBandName()
        }
    }

    private fun generateBandName() {
        val firstName = binding.firstNameTextInput.editText?.text.toString()
        val lastName = binding.lastNameTextInput.editText?.text.toString()

        Log.d("MainActivity", "First Name: $firstName, Last Name: $lastName")

        if (firstName.isNotEmpty() && lastName.length >= 3) {
            val bandName = BandName(firstName, lastName)
            binding.bandNameTextView.text = bandName.name
            Log.d("MainActivity", "Generated Band Name: ${bandName.name}")
        } else {
            binding.bandNameTextView.text = "Please enter a valid first name and a last name with at least 3 characters"
            Log.d("MainActivity", "Invalid input")
        }
    }
}