package ru.spbau.mit.dictionary.main

import java.io.Serializable

class Word(public val id: Int, public val word: String, public var translates: List<String>,
           public var img: ByteArray?, public var priority: Int) : Serializable {
    constructor(id: Int, word: String) : this(id, word, emptyList(), null, 0)
    constructor(id: Int) : this(id, "")
}