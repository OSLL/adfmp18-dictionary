package dictionary.yandex.api.com

import get.protocol.com.GETRequest
import java.util.*


class Translate : YandexTranslatorAPI() {

    private val request = GETRequest(SERVICE_URL, ENCODING)

    @Throws(Exception::class)
    fun execute(text: String, from: Language, to: Language): WordDescription {
        val parameters = Hashtable<String, String>()
        parameters["key"] = apiKey
        parameters["text"] = text
        parameters["lang"] = from.toString() + "-" + to
        return parseResponse(request.sendGet(parameters))
    }
}
