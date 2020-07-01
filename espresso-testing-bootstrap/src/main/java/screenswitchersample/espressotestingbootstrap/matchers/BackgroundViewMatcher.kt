package screenswitchersample.espressotestingbootstrap.matchers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withBackgroundColor(colorHex: String): Matcher<View> {
    return BackgroundColorMatcher(colorHex)
}

private class BackgroundColorMatcher(private val colorHex: String) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("with colorHex: $colorHex")
    }

    public override fun matchesSafely(view: View): Boolean {
        val color = Color.parseColor(colorHex)
        val background = view.background
        if (background is ColorDrawable) {
            return background.color == color
        }
        return false
    }
}
