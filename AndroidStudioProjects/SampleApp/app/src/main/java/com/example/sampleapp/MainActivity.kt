import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.EditText
import com.example.sampleapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the click listener for the button
        var combineButton = null
        combineButton.setOnClickListener {
            combineNames()
        }
    }

    private fun combineNames() {
        // Get the entered first and last names
        val firstNameEditText = null
        var firstNameEditText = null
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()

        // Combine the names
        val fullName = "$firstName $lastName"

        // Display the combined name in the TextView
        resultTextView.text = fullName
    }
}
