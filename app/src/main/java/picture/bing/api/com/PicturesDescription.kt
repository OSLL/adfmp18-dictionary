package picture.bing.api.com

import java.util.ArrayList

class PicturesDescription {
    private val pictures = ArrayList<PictureDescription>()

    val contentUrlList: List<String>
        get() {
            return pictures
                    .filter { it.contentUrl != null }
                    .map { it.contentUrl!! }
        }

    fun addPictureDescription(picture: PictureDescription) {
        pictures.add(picture)
    }

    fun getPictures(): List<PictureDescription> {
        return ArrayList(pictures)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (description in pictures) {
            builder.append(description).append(System.lineSeparator())
        }
        return builder.toString()
    }
}

class PictureDescription {
    var webSearchUrl: String? = null
    var name: String? = null
    var thumbnailUrl: String? = null
    var datePublished: String? = null
    var contentUrl: String? = null
    var hostPageUrl: String? = null
    var contentSize: String? = null
    var encodingFormat: String? = null
    var hostPageDisplayUrl: String? = null
    var width: Long? = null
    var height: Long? = null
    var thumbnail: Thumbnail? = null
    var imageInsightsToken: String? = null
    var insightsMetadata: Metadata? = null
    var imageId: String? = null
    var accentColor: String? = null

    override fun toString(): String {
        return String.format("web search url: %s%n", webSearchUrl) +
                String.format("name: %s%n", name) +
                String.format("thumbnail url: %s%n", thumbnailUrl) +
                String.format("date published: %s%n", datePublished) +
                String.format("content url: %s%n", contentUrl) +
                String.format("host page url: %s%n", hostPageUrl) +
                String.format("content size: %s%n", contentSize) +
                String.format("encoding format: %s%n", encodingFormat) +
                String.format("host page display url: %s%n", hostPageDisplayUrl) +
                String.format("width: %d%n", width) +
                String.format("height: %d%n", height) +
                thumbnail +
                String.format("image insights token: %s%n", imageInsightsToken) +
                insightsMetadata +
                String.format("image id: %s%n", imageId) +
                String.format("accent color: %s%n", accentColor)
    }
}

class Metadata {
    var pagesIncludingCount: Long? = null
    var availableSizesCount: Long? = null

    override fun toString(): String {
        return String.format("metadata: pages including count = %d available sizes count = %d%n", pagesIncludingCount, availableSizesCount)
    }
}

class Thumbnail {
    var width: Long? = null
    var height: Long? = null

    override fun toString(): String {
        return String.format("thumbnail: width = %d height = %d%n", width, height)
    }
}
