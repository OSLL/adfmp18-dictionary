package ru.spbau.mit.dictionary.study

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.spbau.mit.data.DictionaryProvider
import ru.spbau.mit.dictionary.AddWordActivity
import ru.spbau.mit.dictionary.R

class TestWordActivityTest {

    @get:Rule
    public val testActivity: ActivityTestRule<TestWordActivity> =
            ActivityTestRule<TestWordActivity>(TestWordActivity::class.java, false, false)

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

    /*
    if we answer correct, the background color of edittext changed to GREEN
     */
    @Test
    public fun testCorrectAnswer() {
        addWordActivity.launchActivity(null)
        Espresso.onView(ViewMatchers.withId(R.id.saveWord)).perform(ViewActions.click())
        addWordActivity.finishActivity()

        testActivity.launchActivity(null)
        onView(withId(R.id.editText)).perform(replaceText("кошка"))
        onView(withId(R.id.checkButton)).perform(click())
        onView(withId(R.id.editText)).check { view, _ ->
            Assert.assertEquals(Color.GREEN, (view.background as ColorDrawable).color)
        }
        testActivity.finishActivity()
    }


    /*
    if we answer incorrect, the background color of edittext changed to RED
     */
    @Test
    public fun testIncorrectAnswer() {
        addWordActivity.launchActivity(null)
        Espresso.onView(ViewMatchers.withId(R.id.saveWord)).perform(ViewActions.click())
        addWordActivity.finishActivity()

        testActivity.launchActivity(null)
        onView(withId(R.id.editText)).perform(replaceText("неправильно"))
        onView(withId(R.id.checkButton)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.editText)).check { view, _ ->
            Assert.assertEquals(Color.RED, (view.background as ColorDrawable).color)
        }
        testActivity.finishActivity()
    }
}