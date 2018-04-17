package picture.bing.api.com

import org.junit.Test

import org.junit.Assert.*

class BingPictureTest {

    @Test
    fun execute() {
        val bingPicture = BingPicture()
        val picturesDescription = bingPicture.execute("dog", 10, 0)
        assertTrue(picturesDescription.contentUrlList.size >= 10)
    }

    @Test
    fun execute1() {
        val bingPicture = BingPicture()
        val picturesDescription = bingPicture.execute("dog", -10, -5)
        assertTrue(picturesDescription.contentUrlList.size >= 1)
    }
}