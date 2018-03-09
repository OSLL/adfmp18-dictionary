package picture.bing.api.com

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

open class BingPictureAPI {

    protected val SERVICE_URL = "https://api.cognitive.microsoft.com/bing/v7.0/images/search"
    protected val ENCODING = "UTF-8"
    protected lateinit var apiKey: String

    fun setKey(key: String) {
        apiKey = key
    }

    @Throws(Exception::class)
    fun parseResponse(response: String): PicturesDescription {
        val picturesDescription = PicturesDescription()
        val parser = JSONParser()
        val root = parser.parse(response) as JSONObject
        val values = root["value"] as JSONArray
        for (value in values as Iterable<JSONObject>) {
            val picture = PictureDescription()
            parseResult(value, picture)
            picturesDescription.addPictureDescription(picture)
        }

        return picturesDescription
    }

    fun parseResult(__result: JSONObject, picture: PictureDescription) {
        val __metadata = __result["insightsMetadata"] as JSONObject
        val metadata = Metadata()
        parseMetadata(__metadata, metadata)
        picture.insightsMetadata = metadata

        parseInfo(__result, picture)

        val __thumbnail = __result["thumbnail"] as JSONObject
        val thumbnail = Thumbnail()
        parseThumbnail(__thumbnail, thumbnail)
        picture.thumbnail = thumbnail
    }

    fun parseMetadata(__metadata: JSONObject, metadata: Metadata) {
        metadata.availableSizesCount = (__metadata["availableSizesCount"] as Long)
        metadata.pagesIncludingCount = (__metadata["pagesIncludingCount"] as Long)
    }

    fun parseInfo(__info: JSONObject, pictureDescription: PictureDescription) {
        pictureDescription.webSearchUrl = __info["webSearchUrl"] as String
        pictureDescription.name = __info["name"] as String
        pictureDescription.thumbnailUrl = __info["thumbnailUrl"] as String
        pictureDescription.datePublished = __info["datePublished"] as String
        pictureDescription.contentUrl = __info["contentUrl"] as String
        pictureDescription.hostPageUrl = __info["hostPageUrl"] as String
        pictureDescription.contentSize = __info["contentSize"] as String
        pictureDescription.encodingFormat = __info["encodingFormat"] as String
        pictureDescription.hostPageDisplayUrl = __info["hostPageDisplayUrl"] as String
        pictureDescription.width = __info["width"] as Long
        pictureDescription.height = __info["height"] as Long
        pictureDescription.imageInsightsToken = __info["imageInsightsToken"] as String
        pictureDescription.imageId = __info["imageId"] as String
        pictureDescription.accentColor = __info["accentColor"] as String
    }

    fun parseThumbnail(__thumbnail: JSONObject, thumbnail: Thumbnail) {
        thumbnail.height = __thumbnail["height"] as Long
        thumbnail.width = __thumbnail["width"] as Long
    }

}
