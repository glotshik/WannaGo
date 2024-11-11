package edu.elliott.hipsterbands

data class BandName(val lastName: String) {

    private val name_array_1 = arrayOf["Arial", "Bauhaus", "Calibri", "Desdemona", "Exo", "Futura", "Gill Sans", "Helvetica", "Impact", "Janda", "Krungthep", "Lucida", "Myriad", "Newon", "Optima", "Palatino", "Quigly", "Rockwell", "Subway", "Tahoma", "Ultra", "Verdana", "Wingding", "Xefora", "Yukon", "Zapf"]
    private val name_array_2 = arrayOf("Audio", "Beard", "Combover", "Digital", "Eight-track", "Freelance", "Guacamole", "Hang Glider", "Incident", "Jump Suit", "Kybosh", "Lounge", "Moustache", "Novel", "Owl", "Plaid", "Quiff", "Retro", "Spectacles", "Trampoline", "Ukulele", "Video", "Wheat Grass", "Xbox", "Yogurt", "Zombie");

    var name: String = ""

    //
    init {
        firstPart = name_array_1[findArrayPositionFromSource(firstName, 0)]
        secondPart = name_array_2[findAlphaPositionFromSource(lastName, 3)]
        name = firstPart + secondPart
    }

    // member function
    fun findAlphaPositionFromSource(sourceString: String, position: Float) {
        val character: Char = sourceString[position]
        val alphabet: String = "abcdefghijklmnopqrstuvwxyz"
        val alphaPosition = alphabet.indexOf(character, 0)
        return alphaPosition
    }

}