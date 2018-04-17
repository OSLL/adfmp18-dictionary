package dictionary.yandex.api.com

import org.junit.Test

import org.junit.Assert.*

class TranslateTest {

    @Test
    fun execute() {
        val translate = Translate()
        translate.setKey("dict.1.1.20160306T093514Z.bbb9e3db01cef073.d0356e388174436a1f2c93cce683819103ec4579")
        val wordDescription = translate.execute("dog", Language.ENGLISH, Language.RUSSIAN)
        assertTrue(wordDescription.ranking.size > 0)
    }
}