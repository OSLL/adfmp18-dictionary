package ru.spbau.mit.dictionary

import android.database.Cursor
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.CursorMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import ru.spbau.mit.data.DictionaryContract

class MainActivityTest {

    @get:Rule
    public val mainActivity: ActivityTestRule<MainActivity> =
            ActivityTestRule(MainActivity::class.java)

    @Test
    public fun testWordDelete() {
        onData(allOf(`is`(instanceOf(Cursor::class.java)),
                CursorMatchers.withRowString(1, "empty")))
    }
}