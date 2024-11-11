package com.example.geoquizapp

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquizapp.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result of CheatActivity
        if (result.resultCode == Activity.RESULT_OK) {
            val isCheater = result.data?.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false) ?: false
            if (isCheater) {
                // Mark the current question as cheated and decrement tokens
                quizViewModel.markCurrentQuestionCheated()
                quizViewModel.decrementCheatTokens()
                updateCheatButton()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)

        } // Do something in response to the click here

        binding.falseButton.setOnClickListener { view: View ->
           checkAnswer(false)
        }

        binding.nextButton.setOnClickListener { view: View ->
         quizViewModel.moveToNext()
            updateQuestion()
        }


        binding.cheatButton.setOnClickListener {
            // Start CheatActivity if cheating is allowed
            if (quizViewModel.canCheat()) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                cheatLauncher.launch(intent)
            } else {
                Toast.makeText(this, R.string.no_cheats_left, Toast.LENGTH_SHORT).show()
            }
        }

        updateQuestion()
        updateCheatButton()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        updateAnswerButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when{
            quizViewModel.isCurrentQuestionCheated  -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show()

        quizViewModel.markCurrentQuestionAnswered()

        updateAnswerButtons()
    }
@RequiresApi(Build.VERSION_CODES.S)
    private fun updateAnswerButtons() {
        val currentQuestionAnswered = quizViewModel.isCurrentQuestionAnswered
        binding.trueButton.isEnabled = !currentQuestionAnswered
        binding.falseButton.isEnabled = !currentQuestionAnswered
    }

    private fun updateCheatButton() {
        binding.cheatButton.isEnabled = quizViewModel.remainingCheatTokens > 0
        binding.cheatTokensTextView.text = getString(R.string.cheat_tokens_remaining, quizViewModel.remainingCheatTokens)
    }

    // lifecycle methods
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


}