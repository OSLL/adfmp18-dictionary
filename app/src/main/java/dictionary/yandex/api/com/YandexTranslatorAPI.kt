package dictionary.yandex.api.com

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


open class YandexTranslatorAPI {
    protected val SERVICE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup"
    protected val ENCODING = "UTF-8"
    protected lateinit var apiKey: String

    fun setKey(key: String) {
        apiKey = key
    }

    @Throws(Exception::class)
    fun parseResponse(response: String): WordDescription {
        val wordDescription = WordDescription()
        val parser = JSONParser()
        val root = parser.parse(response) as JSONObject
        val def = root.get("def") as JSONArray
        val iterator = def.iterator()
        while (iterator.hasNext()) {
            val rank = Rank()
            parseRank(iterator.next() as JSONObject, rank)
            wordDescription.addRank(rank)
        }
        return wordDescription
    }

    fun parseRank(def: JSONObject, rank: Rank) {
        rank.text = def["text"] as String
        rank.partOfSpeech = def["pos"] as String
        rank.transcription = def["ts"] as String
        val tr = def.get("tr") as JSONArray
        val iterator = tr.iterator()
        while (iterator.hasNext()) {
            val translation = Translation()
            parseTranslation(iterator.next() as JSONObject, translation)
            rank.addTranslation(translation)
        }
    }

    fun parseTranslation(tr: JSONObject, translation: Translation) {
        translation.text = tr["text"] as String?
        translation.gen = tr["gen"] as String?
        translation.partOfSpeech = tr["pos"] as String?
        run {
            if (tr["syn"] != null) {
                val syn = tr["syn"] as JSONArray
                val iterator = syn.iterator()
                while (iterator.hasNext()) {
                    val synonym = Synonym()
                    parseSynonym(iterator.next() as JSONObject, synonym)
                    translation.addSynonym(synonym)
                }
            }
        }
        run {
            if (tr["mean"] != null) {
                val mean = tr["mean"] as JSONArray
                val iterator = mean.iterator()
                while (iterator.hasNext()) {
                    val meaning = Meaning()
                    parseMeaning(iterator.next() as JSONObject, meaning)
                    translation.addMeaning(meaning)
                }
            }
        }

        run {
            if (tr["ex"] != null) {
                val ex = tr["ex"] as JSONArray
                val iterator = ex.iterator()
                while (iterator.hasNext()) {
                    val example = Example()
                    parseExample(iterator.next() as JSONObject, example)
                    translation.addExample(example)
                }
            }
        }

    }

    fun parseSynonym(syn: JSONObject, synonym: Synonym) {
        synonym.text = syn["text"] as String?
        synonym.gen = syn["gen"] as String?
        synonym.partOfSpeech = syn["pos"] as String?
    }

    fun parseMeaning(mean: JSONObject, meaning: Meaning) {
        meaning.text = mean["text"] as String?
    }

    fun parseExample(ex: JSONObject, example: Example) {
        example.text = ex["text"] as String?
        val tr = ex["tr"] as JSONArray
        val iterator = tr.iterator()
        while (iterator.hasNext()) {
            example.addTranslate((iterator.next() as JSONObject)["text"] as String)
        }
    }
}
