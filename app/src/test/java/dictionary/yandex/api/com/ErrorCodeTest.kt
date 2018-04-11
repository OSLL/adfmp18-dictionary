package dictionary.yandex.api.com

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Oleg on 4/11/2018.
 */
class ErrorCodeTest {
    @Test
    fun toStringTest() {
        assertEquals("""
            |Value: 200
            |Description: Operation completed successfully
            """.trimMargin(), ErrorCode.ERR_OK.toString())
        assertEquals("""
            |Value: 401
            |Description: Invalid API key
            """.trimMargin(), ErrorCode.ERR_KEY_INVALID.toString())
        assertEquals("""
            |Value: 402
            |Description: This API key has been blocked
            """.trimMargin(), ErrorCode.ERR_KEY_BLOCKED.toString())
        assertEquals("""
            |Value: 403
            |Description: Exceeded the daily limit on the number of requests
            """.trimMargin(), ErrorCode.ERR_DAILY_REQ_LIMIT_EXCEEDED.toString())
        assertEquals("""
            |Value: 413
            |Description: The text size exceeds the maximum
            """.trimMargin(), ErrorCode.ERR_TEXT_TOO_LONG.toString())
        assertEquals("""
            |Value: 501
            |Description: The specified translation direction is not supported
            """.trimMargin(), ErrorCode.ERR_LANG_NOT_SUPPORTED.toString())
    }

    @Test
    fun fromValue() {
        assertEquals(ErrorCode.fromValue(200), ErrorCode.ERR_OK)
        assertEquals(ErrorCode.fromValue(401), ErrorCode.ERR_KEY_INVALID)
        assertEquals(ErrorCode.fromValue(402), ErrorCode.ERR_KEY_BLOCKED)
        assertEquals(ErrorCode.fromValue(403), ErrorCode.ERR_DAILY_REQ_LIMIT_EXCEEDED)
        assertEquals(ErrorCode.fromValue(413), ErrorCode.ERR_TEXT_TOO_LONG)
        assertEquals(ErrorCode.fromValue(501), ErrorCode.ERR_LANG_NOT_SUPPORTED)
    }

}