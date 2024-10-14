package com.example.geoquizapp

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

//private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
// Add a new constant for the remaining cheat tokens key
const val REMAINING_CHEAT_TOKENS_KEY = "REMAINING_CHEAT_TOKENS_KEY"
const val CHEATED_QUESTIONS_KEY = "CHEATED_QUESTIONS_KEY"


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_america, true),
        Question(R.string.question_africa, true),
        Question(R.string.question_mideast, true),
        Question(R.string.question_asia, true)
    )

    // Track the current question index
    var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    // Add a new property for remaining cheat tokens
     var remainingCheatTokens: Int
         get() = savedStateHandle.get(REMAINING_CHEAT_TOKENS_KEY) ?: 3
         private set(value) = savedStateHandle.set(REMAINING_CHEAT_TOKENS_KEY, value)

    // Track which questions have been cheated on
    private var cheatedQuestions: MutableSet<Int>
        get() = savedStateHandle.get(CHEATED_QUESTIONS_KEY) ?: mutableSetOf()
        set(value) = savedStateHandle.set(CHEATED_QUESTIONS_KEY, value)


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    // Check if the current question has been cheated on
    val isCurrentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered

    // property to check if the current question has been cheated on
    val isCurrentQuestionCheated: Boolean
        get() = currentIndex in cheatedQuestions

    fun moveToNext() {
//        Log.d(TAG, "Updating question text", Exception())
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }

    fun markCurrentQuestionAnswered() {
        questionBank[currentIndex].isAnswered = true
    }

    // Add a new function to decrement cheat tokens
     fun decrementCheatTokens() {
         if (remainingCheatTokens > 0) {
             remainingCheatTokens--
         }
     }
    // Mark the current question as cheated
    fun markCurrentQuestionCheated() {
        cheatedQuestions = (cheatedQuestions + currentIndex).toMutableSet()
    }
    // Add a function to check if cheating is allowed
     fun canCheat(): Boolean {
         return remainingCheatTokens > 0
     }



}