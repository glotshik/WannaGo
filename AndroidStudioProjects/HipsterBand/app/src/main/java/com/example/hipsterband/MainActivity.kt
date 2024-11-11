package edu.elliott.hipsterbands

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declare the variables for views
    private lateinit var firstNameText: EditText
    private lateinit var lastNameText: EditText
    private lateinit var bandNameText: TextView
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Wire up widgets using View Bindings (recommended) or findViewById
        // Note: For simplicity, I'll continue using findViewById in this example
        firstNameText = findViewById(R.id.first_name_text_input)
        lastNameText = findViewById(R.id.last_name_text_input)
        bandNameText = findViewById(R.id.band_name_text_view)
        submitButton = findViewById(R.id.submit_button)

        // Set onClickListener for the button
        submitButton.setOnClickListener { view: View ->
            // Get the user input from EditText fields
            val firstName = firstNameText.text.toString().trim()
            val lastName = lastNameText.text.toString().trim()

            // Check if input is valid
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                // Calculate band name based on rules
                val bandName = calculateBandName(firstName, lastName)

                // Display the band name in the TextView
                bandNameText.text = bandName
            } else {
                // Handle invalid input (optional)
                bandNameText.text = "Please enter both first and last names."
            }
        }
    }

    private fun calculateBandName(firstName: String, lastName: String): String {
        // Implement your logic to calculate the band name based on the specified rules
        // For example, you can use the FIRST letter of the first name and the THIRD letter of the last name
        // Ensure that the indices are within the bounds of the strings

        val firstLetter = if (firstName.length >= 1) firstName[0].toUpperCase() else ""
        val thirdLetter = if (lastName.length >= 3) lastName[2].toUpperCase() else ""

        return "$firstLetter$thirdLetter Band"
    }
}
