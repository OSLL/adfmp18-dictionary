package dictionary.yandex.api.com

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ExampleTest {

    lateinit var example : Example

    @Before
    fun before() {
        example = Example()
    }

    @Test
    fun getText() {
        assertEquals("", example.text)
    }

    @Test
    fun setText() {
        example.text = "example"
        assertEquals("example", example.text)
    }

    @Test
    fun getTranlations() {
        assertEquals(true, example.tranlations.isEmpty())
        assertEquals(0, example.tranlations.size)
    }

    @Test
    fun addTranslate() {
        example.addTranslate("книга")
        example.addTranslate("журнал")
        assertEquals("книга", example.tranlations[0])
        assertEquals("журнал", example.tranlations[1])
    }

    @Test
    fun toStringTest() {
        example.text = "dog"
        example.addTranslate("собака")
        example.addTranslate("псина")
        assertEquals("dog - собака, псина", example.toString())
    }
}