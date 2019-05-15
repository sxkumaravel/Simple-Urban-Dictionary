package com.demo.simpleurbandictionary

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import com.demo.simpleurbandictionary.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not

/**
 * Created on 2019-05-14.
 *
 * @author kumars
 */
@MediumTest
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun searchDictionary() {
        onView(withId(R.id.search_view)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("world"), pressImeActionButton())
        // check whether the recycler view is displayed - since
        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun searchDictionaryInvalidChar() {
        onView(withId(R.id.search_view)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("wsvdfvdfvorld"), pressImeActionButton())
        onView(withText(R.string.no_results_found)).inRoot(RootMatchers.withDecorView(not(`is`(mActivityRule.activity.window.decorView))))
    }
}