package get.protocol.com

import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.ClassCastException
import java.util.*


class GETRequestTest {

    @Test
    fun sendGet() {
        val request = GETRequest("https://www.yandex.ru", "UTF-8")
        val parameters = Hashtable<String, String>()
        assertTrue(request.sendGet(parameters).isNotEmpty())
    }

    @Test(expected = ClassCastException::class)
    fun sendGet1() {
        val request = GETRequest("http://www.yandex.ru", "UTF-8")
        val parameters = Hashtable<String, String>()
        assertTrue(request.sendGet(parameters).isNotEmpty())
    }
}