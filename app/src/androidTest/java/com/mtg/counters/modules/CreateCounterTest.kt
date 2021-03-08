package com.mtg.counters.modules


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.mtg.counters.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateCounterTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createCounterTest() {
        val appCompatButton = onView(
                allOf(withId(R.id.btn_start), withText("GET STARTED"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_fragment),
                                        0),
                                3),
                        isDisplayed()))
        appCompatButton.perform(click())

        val appCompatButton2 = onView(
                allOf(withId(R.id.btn_add_counter), withText("ADD COUNTER"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_fragment),
                                        0),
                                8),
                        isDisplayed()))
        appCompatButton2.perform(click())

        val appCompatTextView = onView(
                allOf(withId(R.id.txt_see_examples), withText("Give it a name. Creative block? See examples."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_fragment),
                                        0),
                                2),
                        isDisplayed()))
        appCompatTextView.perform(click())

        val recyclerView = onView(
                allOf(withId(R.id.rcv_food),
                        childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                5)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatTextView2 = onView(
                allOf(withId(R.id.txt_save_item), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar_create_item),
                                        0),
                                2),
                        isDisplayed()))
        appCompatTextView2.perform(click())

        val appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatButton3.perform(scrollTo(), click())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
