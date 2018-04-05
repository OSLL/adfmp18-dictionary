package picture.bing.api.com

import android.util.Log
import get.protocol.com.GETRequest
import java.util.*

class BingPicture : BingPictureAPI() {

    private val request = GETRequest(SERVICE_URL, ENCODING)

    @Throws(Exception::class)
    @JvmOverloads
    fun execute(text: String, count: Int, offset: Int = 0): PicturesDescription {
        val parameters = Hashtable<String, String>()
        parameters.put("q", "'$text'")
        parameters.put("\$format", "json")
        parameters.put("\$top", Integer.toString(count))
        parameters.put("\$skip", Integer.toString(offset))
        val requestProperty = Hashtable<String, String>()
        requestProperty.put("Ocp-Apim-Subscription-Key", "2f7eed87304f41298e2874464892f418")
        val s = request.sendGet(parameters, requestProperty)
        Log.d("PICTURE", s)
        return parseResponse(s)
    }
}
