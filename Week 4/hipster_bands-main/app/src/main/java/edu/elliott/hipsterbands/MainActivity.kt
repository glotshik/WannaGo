package edu.elliott.hipsterbands

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/*
This app will ask the user for his/her name and will then calculate a value based on the input.
In this case, the app will produce a hipster band name.
The band name is calculated by finding values in set arrays based on the FIRST letter of the user's first name and the THIRD letter of the user's last name.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Wire up widgets
        // FIXME: Change to use View Bindings instead of findViewById
        submitButton = findViewById(R.id.submit_button)
        firstNameText = findViewById(R.id.first_name_label)
        lastNameText = findViewById(R.id.last_name_label)
        bandNameText = findViewById(R.id.band_name_text_view)

        // Set onClickListenersfor button
        submitButton.setOnClickListener { view: View ->
            val bandName: BandName = BandName(firstNameText.value, lastNameText.value)
            bandNameText.set(bandName.name)
        }
    }

}