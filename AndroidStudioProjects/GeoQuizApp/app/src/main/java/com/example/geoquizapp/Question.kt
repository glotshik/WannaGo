package com.example.geoquizapp

import androidx.annotation.StringRes
//data class Question(@StringRes val textResId: Int, val answer: Boolean)
data class Question(val textResId: Int, val answer: Boolean, var isAnswered: Boolean = false)