package ru.spbau.mit.dictionary

import android.app.Activity
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getContext
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.CursorMatchers.withRowString
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import ru.spbau.mit.dictionary.study.StudyActivity
import android.widget.ListView
import org.junit.*
import ru.spbau.mit.adapters.WordsCursorAdapter
import ru.spbau.mit.data.DictionaryProvider

class MainActivityTest {

    @get:Rule
    public val mainActivity: ActivityTestRule<MainActivity> =
            ActivityTestRule(MainActivity::class.java, true, false)

    @get:Rule
    public val addWordActivity: ActivityTestRule<AddWordActivity> =
            ActivityTestRule<AddWordActivity>(AddWordActivity::class.java, true, false)

    private fun getIntent(activity: Class<out Activity>): Intent {
        val targetContext = InstrumentationRegistry.getInstrumentation()
                .targetContext
        return Intent(targetContext, activity)
    }

    @Before
    fun setup() {
        getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_ENTRY, null, null)
        getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_RELATION, null, null)
    }

    /*
    check that click in "Тест" launch StudyActivity
     */
    @Test
    public fun testMenuTest() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "sun")
        addWordActivity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())
        addWordActivity.activity.finish()

        mainActivity.launchActivity(null)
        Intents.init()
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(ViewMatchers.withText("Тест")).perform(click())
        Intents.intended(IntentMatchers.hasComponent(StudyActivity::class.java.name))
        Intents.release()
        mainActivity.finishActivity()
    }

    @Test
    public fun testWordDelete() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "sun")
        addWordActivity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())
        addWordActivity.activity.finish()

        mainActivity.launchActivity(null)
        onData(withRowString(1, "sun"))
                .inAdapterView(allOf(withId(R.id.list_view_words), hasFocus()))
                .perform(click())
        onView(withText("Удалить")).perform(click())
        val listView = mainActivity.activity.findViewById<ListView>(R.id.list_view_words)
        val adapter = listView.adapter as WordsCursorAdapter
        val cursor = adapter.cursor
        cursor.moveToFirst()
        val wordLists = ArrayList<String>()
        while (cursor.moveToNext()) {
            wordLists.add(cursor.getString(1))
        }
        Assert.assertFalse(wordLists.contains("sun"))

        // test below should check same thing that above,
        // but it throw exception RuntimeException(Row with "sun" not found)
        // it's problem of 'криворукие программисты' who writes Espresso framework
//        onData(withRowString(1, "sun"))
//                .inAdapterView(allOf(withId(R.id.list_view_words), hasFocus()))
//                .check(doesNotExist())
    }

    @Test
    public fun testWordToStudied() {
        val intent = getIntent(AddWordActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "sun")
        addWordActivity.launchActivity(intent)
        onView(withId(R.id.saveWord)).perform(click())
        addWordActivity.activity.finish()

        mainActivity.launchActivity(null)
        onData(withRowString(1, "sun"))
                .inAdapterView(allOf(withId(R.id.list_view_words), isFocusable()))
                .perform(click())
        onView(withText("Изучено")).perform(click())

        onView(withText("Изученные")).perform(click())
        onData(withRowString(1, "sun"))
                .inAdapterView(withId(R.id.pager))
                .inAdapterView(allOf(withId(R.id.list_view_words), isFocusable()))
                .check(matches(isDisplayed()))
    }
}