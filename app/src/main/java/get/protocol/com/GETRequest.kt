package get.protocol.com

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection

class GETRequest(private val url: String, private val encoding: String) {

    @Throws(Exception::class)
    @JvmOverloads
    fun sendGet(parameters: Hashtable<String, String>?, requestProperty: Hashtable<String, String>? = null): String {
        var firstStep = true
        var url = this.url
        if (parameters != null) {
            val keys = parameters.keys()
            while (keys.hasMoreElements()) {
                var key = keys.nextElement()
                val value = URLEncoder.encode(parameters[key], encoding)
                key = URLEncoder.encode(key, encoding)
                url += (if (firstStep) '?' else '&') + key + '=' + value
                firstStep = false
            }
        }

        val urlObject = URL(url)
        val connection = urlObject.openConnection() as HttpsURLConnection
        connection.connectTimeout = Companion.TIME_OUT
        connection.readTimeout = Companion.TIME_OUT
        connection.setRequestProperty("Content-Type", "text/plain; charset=" + encoding)
        connection.setRequestProperty("Accept-Charset", encoding)

        if (requestProperty != null) {
            val requestKeys = requestProperty.keys()
            while (requestKeys.hasMoreElements()) {
                val key = requestKeys.nextElement()
                val value = requestProperty[key]
                connection.setRequestProperty(key, value)
            }
        }

        connection.requestMethod = "GET"
        BufferedReader(InputStreamReader(connection.inputStream)).use {
            return it.readText()
        }
    }

    companion object {
        private var TIME_OUT = 5000
    }
}
