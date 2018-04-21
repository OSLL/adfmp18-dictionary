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
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.CursorMatchers.withRowString
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import org.junit.Before
import ru.spbau.mit.data.DictionaryProvider


@RunWith(AndroidJUnit4::class)
@LargeTest
public class AddWordActivityTest {

    @get:Rule
    public val activity: ActivityTestRule<AddWordActivity> =
            ActivityTestRule<AddWordActivity>(AddWordActivity::class.java, false, false)

    @get:Rule
    public val mainActivity: ActivityTestRule<MainActivity> =
            ActivityTestRule(MainActivity::class.java, true, false)

    private fun getIntent(activity: Class<out Activity>): Intent {
        val targetContext = InstrumentationRegistry.getInstrumentation()
                .targetContext
        return Intent(targetContext, activity)
    }

    @Before
    fun setup() {
        InstrumentationRegistry.getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_ENTRY, null, null)
        InstrumentationRegistry.getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_RELATION, null, null)
    }

    /*
    if we found translation we show it in wordTextView
     */
    @Test
    public fun testTranslateWord() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "cat")
        activity.launchActivity(intent)

        onView(withId(R.id.wordTextView)).check(matches(withText(containsString("cat -> кошка"))))
        activity.finishActivity()
    }

    /*
    if cannot find translate of word show Problem with translate
     */
    @Test
    public fun testWordNotFoundTranslate() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "ошибка")
        activity.launchActivity(intent)
        onView(withId(R.id.wordTextView)).check(matches(withText(containsString("Problem with translation"))))
        activity.finishActivity()
    }

    /*
    if word added before hide save button
     */
    @Test
    public fun testSaveWord() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "sky")
        activity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())
        activity.finishActivity()
        activity.launchActivity(intent)
        onView(withId(R.id.saveWord)).check(matches(not(isDisplayed())))
        activity.finishActivity()
    }

    /*
    saved word are displayed in study tab
     */
    @Test
    public fun testSaveWordInMainActivity() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "sun")
        activity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())
        activity.finishActivity()

        mainActivity.launchActivity(null)
        onData(allOf(withRowString(1, "sun")))
                .inAdapterView(allOf(withId(R.id.list_view_words), isFocusable())).check(matches(isDisplayed()))

        mainActivity.finishActivity()
    }

}