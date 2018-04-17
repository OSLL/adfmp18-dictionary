package ru.spbau.mit.dictionary.main

import java.io.Serializable

class Word(public val id: Int, public val word: String, public var translates: List<String>,
           public var img: ByteArray?) : Serializable {
    constructor(id: Int, word: String) : this(id, word, emptyList(), null)
    constructor(id: Int) : this(id, "")
}