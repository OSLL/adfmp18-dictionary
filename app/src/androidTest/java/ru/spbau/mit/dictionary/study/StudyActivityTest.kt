package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.widget.TextView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.AddWordActivity
import ru.spbau.mit.dictionary.R

class StudyActivityTest {
    @get:Rule
    public val studyActivity: IntentsTestRule<StudyActivity> =
            IntentsTestRule<StudyActivity>(StudyActivity::class.java, false, false)

    @get:Rule
    public val addWordActivity: ActivityTestRule<AddWordActivity> =
            object: ActivityTestRule<AddWordActivity>(AddWordActivity::class.java, false, false) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation()
                            .targetContext
                    val intent =  Intent(targetContext, AddWordActivity::class.java)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "cat")
                    return intent
                }
            }

    @Before
    fun setup() {
        InstrumentationRegistry.getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_ENTRY, null, null)
        InstrumentationRegistry.getContext().contentResolver.delete(DictionaryProvider.CONTENT_WORDS_RELATION, null, null)
    }

    @Test
    public fun testLearnActivity() {
        addWordActivity.launchActivity(null)
        Espresso.onView(ViewMatchers.withId(R.id.saveWord)).perform(click())
        addWordActivity.finishActivity()

        studyActivity.launchActivity(null)
        onView(allOf(withId(R.id.wordView), `is`(instanceOf(TextView::class.java))))
                .check(matches(matchWithText("cat")))
    }

    @Test
    public fun testStartedActivity() {
        addWordActivity.launchActivity(null)
        onView(withId(R.id.saveWord)).perform(click())
        addWordActivity.finishActivity()

        studyActivity.launchActivity(null)
        onView(withId(R.id.next)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(StartTestWordActivity::class.java.name))

    }

    private fun matchWithText(text: String): BaseMatcher<View> {
        return object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) {
                description!!.appendText("TextView with text = $text")
            }

            override fun matches(item: Any?): Boolean {
                if (item is TextView) {
                    return item.text.toString().contains(text)
                }
                return false
            }
        }
    }

}