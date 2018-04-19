package ru.spbau.mit.dictionary

import android.app.Activity
import android.content.Intent
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.CursorMatchers
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import android.widget.ListView
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString
import ru.spbau.mit.dictionary.main.TabStudy


@RunWith(AndroidJUnit4::class)
@LargeTest
public class AddWordActivityTest {

    @get:Rule
    public val activity: ActivityTestRule<AddWordActivity> =
            ActivityTestRule<AddWordActivity>(AddWordActivity::class.java, true, false)

    @get:Rule
    public val mainActivity: ActivityTestRule<MainActivity> =
            ActivityTestRule(MainActivity::class.java, true, false)

    private fun getIntent(activity: Class<out Activity>): Intent {
        val targetContext = InstrumentationRegistry.getInstrumentation()
                .targetContext
        return Intent(targetContext, activity)
    }

    @Test
    public fun testTranslateWord() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "cat")
        activity.launchActivity(intent)

        onView(withId(R.id.wordTextView)).check(matches(withText(containsString("cat -> кошка"))))
    }

    @Test
    public fun testWordNotFoundTranslate() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "ошибка")
        activity.launchActivity(intent)
        onView(withId(R.id.wordTextView)).check(matches(withText(containsString("Problem with translation"))))
    }

    @Test
    public fun testSaveWord() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "word")
        activity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())

        val mainIntent = getIntent(MainActivity::class.java)
        mainActivity.launchActivity(null)
        onData(anything()).inAdapterView(withId(R.id.list_view)).atPosition(0).inAdapterView(withId(R.id.list_view))
//        onView(withId(R.id.list_view)).check(matches(withText(containsString("word"))))

    }
}