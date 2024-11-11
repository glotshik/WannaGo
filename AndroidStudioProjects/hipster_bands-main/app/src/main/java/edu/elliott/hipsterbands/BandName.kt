package edu.elliott.hipsterbands

import android.util.Log

class BandName(firstName: String, lastName: String) {
    val name: String

    init {
        val firstWord = getFirstWord(firstName.firstOrNull()?.uppercaseChar() ?: 'A')
        val secondWord = getSecondWord(lastName.getOrNull(2)?.uppercaseChar() ?: 'A')
        name = "$firstWord $secondWord"
        Log.d("BandName", "Generated name: $name")
    }

    private fun getFirstWord(letter: Char): String {
        val words = arrayOf("Rusty", "Jazzy", "Groovy", "Funky", "Mellow", "Cosmic", "Electric", "Vintage", "Velvet", "Neon")
        return words[letter.code % words.size]
    }

    private fun getSecondWord(letter: Char): String {
        val words = arrayOf("Whiskers", "Banjo", "Harmonica", "Saxophone", "Ukulele", "Tambourine", "Xylophone", "Kazoo", "Accordion", "Theremin")
        return words[letter.code % words.size]
    }
}