package com.jaynewstrom.screenswitchersample.activity

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withTextView(
    id: Int,
    text: String,
    visibility: ViewMatchers.Visibility = ViewMatchers.Visibility.VISIBLE
): Matcher<View> {
    return TextViewMatcher(`is`(id), `is`(text), ViewMatchers.withEffectiveVisibility(visibility))
}

private class TextViewMatcher(
    private val idMatcher: Matcher<Int>,
    private val textMatcher: Matcher<String>,
    private val visibilityMatcher: Matcher<View>
) : BoundedMatcher<View, TextView>(TextView::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("with id: ")
        idMatcher.describeTo(description)
        description.appendText(" with text: ")
        textMatcher.describeTo(description)
        description.appendText(" with visibility: ")
        visibilityMatcher.describeTo(description)
    }

    override fun matchesSafely(textView: TextView): Boolean {
        val id = textView.id
        val text = textView.text.toString()
        return idMatcher.matches(id) && textMatcher.matches(text) && visibilityMatcher.matches(textView)
    }
}
