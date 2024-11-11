package com.example.magic8ball

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.magic8ball.databinding.ActivityMainBinding  // Import the generated binding class

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: Magic8BallViewModel
    private lateinit var binding: ActivityMainBinding  // Declare the binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)  // Initialize the binding
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Magic8BallViewModel::class.java)


        // Button click listener
        binding.btnAsk.setOnClickListener {
            val responses = arrayOf(
                "It is certain",
                "It is decidedly so",
                "Without a doubt",
                "Yes, definitely",
                "You may rely on it",
                "As I see it, yes",
                "Most likely",
                "Outlook good",
                "Yes",
                "Signs point to yes",
                "Reply hazy try again",
                "Ask again later",
                "Better not tell you now",
                "Cannot predict now",
                "Concentrate and ask again",
                "Don't count on it",
                "My reply is no",
                "My sources say no",
                "Outlook not so good",
                "Very doubtful"
            )
            val randomResponse = responses.random()
            val response = Response(randomResponse)

            viewModel.lastResponse = response

            binding.tvResponse.text = randomResponse
        }

        // Display the last response if available during configuration changes
        viewModel.lastResponse?.let {
            binding.tvResponse.text = it.responseText
        }
    }

    // Save ViewModel state during configuration changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.lastResponse?.let {
            outState.putString("lastResponse", it.responseText)
        }
    }

    // Restore ViewModel state during configuration changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastResponseText = savedInstanceState.getString("lastResponse")
        if (!lastResponseText.isNullOrEmpty()) {
            viewModel.lastResponse = Response(lastResponseText)
            binding.tvResponse.text = lastResponseText
        }
    }
}