package dictionary.yandex.api.com

enum class ErrorCode(private val value: Int, private val description: String) {
    ERR_OK(200, "Operation completed successfully"),
    ERR_KEY_INVALID(401, "Invalid API key"),
    ERR_KEY_BLOCKED(402, "This API key has been blocked"),
    ERR_DAILY_REQ_LIMIT_EXCEEDED(403, "Exceeded the daily limit on the number of requests"),
    ERR_TEXT_TOO_LONG(413, "The text size exceeds the maximum"),
    ERR_LANG_NOT_SUPPORTED(501, "The specified translation direction is not supported");

    override fun toString(): String {
        return """
            |Value: $value
            |Description: $description
            """.trimMargin()
    }

    companion object {
        fun fromValue(value: Int): ErrorCode? {
            return values().firstOrNull { it.value == value }
        }
    }
}
