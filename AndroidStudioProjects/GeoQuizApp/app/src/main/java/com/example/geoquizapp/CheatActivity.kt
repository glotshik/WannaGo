package com.example.geoquizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquizapp.databinding.ActivityCheatBinding

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            binding.answerTextView.setText(answerText)
            setAnswerShowResult(true)
        }

        // Add API level information
        binding.apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)
    }

    private fun setAnswerShowResult(isAnswerShow: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShow)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquizzapp.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.example.geoquizzapp.answer_shown"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}