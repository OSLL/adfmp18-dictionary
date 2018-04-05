package dictionary.yandex.api.com

import java.util.*

class WordDescription {

    val ranking = ArrayList<Rank>()

    fun addRank(translation: Rank) {
        ranking.add(translation)
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        for (i in ranking.indices) {
            stringBuffer.append(ranking[i].toString() + "<br /><br />")
        }
        return stringBuffer.toString()
    }
}

class Rank {
    var text: String? = null
    var partOfSpeech: String? = null
    var transcription: String? = null
    val translation = ArrayList<Translation>()

    fun addTranslation(translation: Translation) {
        this.translation.add(translation)
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append("<font color=\"black\">$text</font> ")
        if (transcription != null) {
            stringBuffer.append("[$transcription]")
        }
        if (partOfSpeech != null) {
            stringBuffer.append(" <font color=\"green\">$partOfSpeech</font>")
        }

        for (i in translation.indices) {
            stringBuffer.append("<br />" + (i + 1) + " " + translation[i])
        }
        return stringBuffer.toString()
    }
}

class Translation {
    var text: String? = null
    var partOfSpeech: String? = null
    var gen: String? = null
    val synonyms = ArrayList<Synonym>()
    val meanings = ArrayList<Meaning>()
    val examples = ArrayList<Example>()

    fun addSynonym(synonym: Synonym) {
        synonyms.add(synonym)
    }

    fun addMeaning(meaning: Meaning) {
        meanings.add(meaning)
    }

    fun addExample(example: Example) {
        examples.add(example)
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append("<font color=\"blue\">$text</font>")
        if (gen != null) {
            stringBuffer.append(" " + gen!!)
        }

        for (i in synonyms.indices) {
            stringBuffer.append(", " + synonyms[i])
        }

        if (meanings.size > 0)
            stringBuffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">(")
        for (i in meanings.indices) {
            stringBuffer.append(meanings[i].toString() + if (i != meanings.size - 1) ", " else "")
        }
        if (meanings.size > 0)
            stringBuffer.append(")</font>")

        if (examples.size > 0) {
            stringBuffer.append("<br />")
        }
        for (i in examples.indices) {
            stringBuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + examples[i] + if (i != examples.size - 1) "<br />" else "")
        }

        return stringBuffer.toString()
    }
}

class Synonym {
    var text: String? = null
    var partOfSpeech: String? = null
    var gen: String? = null

    override fun toString(): String {
        return "<font color=\"blue\">" + text + "</font>" + if (gen != null) " " + gen!! else ""
    }
}

class Meaning {
    var text: String? = null

    override fun toString(): String {
        return text.toString()
    }

}

class Example {
    var text: String? = null
    val tranlations = ArrayList<String>()

    fun addTranslate(translate: String) {
        tranlations.add(translate)
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append(text!! + " - ")
        for (i in tranlations.indices) {
            stringBuffer.append(tranlations[i] + if (i != tranlations.size - 1) ", " else "")
        }
        return stringBuffer.toString()
    }
}
