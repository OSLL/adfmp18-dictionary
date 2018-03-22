package ru.spbau.mit.dictionary.main

import java.io.Serializable

class Word(public val word: String, public val translate: List<String>) : Serializable {
    //TODO("add picture maybe")
}