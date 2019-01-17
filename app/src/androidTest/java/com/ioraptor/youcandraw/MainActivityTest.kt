package com.ioraptor.youcandraw

import android.graphics.Rect
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val mainActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)


    @Test
    fun isDrawDisplayed() {
        onView(withId(R.id.userCanvasView)).check(matches(isDisplayed()))
    }

    @Test
    fun canUserDrawAX() {
        onView(withId(R.id.userCanvasView)).perform(xTouchAction())
    }

    private fun xTouchAction(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Send touch events."
            }

            override fun perform(uiController: UiController, view: View) {

                // Recover the view bounds relative to the screen position
                val viewBounds = Rect()
                view.getGlobalVisibleRect(viewBounds)

                // First leg
                val firstLegStart = floatArrayOf(viewBounds.left.toFloat()+1, viewBounds.top.toFloat()+1)
                val firstLegEnd = floatArrayOf(viewBounds.right.toFloat()-1, viewBounds.bottom.toFloat()-1)

                // Second leg
                val secondLegStart = floatArrayOf(viewBounds.right.toFloat()-1, viewBounds.top.toFloat()+1)
                val secondLegEnd = floatArrayOf(viewBounds.left.toFloat()+1, viewBounds.bottom.toFloat()-1)

                // Touch and move by the first leg
                linearTouch(firstLegStart, firstLegEnd, uiController)

                // Touch and move by the first leg
                linearTouch(secondLegStart, secondLegEnd, uiController)

            }

            private fun linearTouch(from: FloatArray, to: FloatArray, uiController: UiController) {
                // Coordinates by view position
                val downCoordinates = floatArrayOf(from[0], from[1])
                val upCoordinates = floatArrayOf(to[0], to[1])
                val precision = floatArrayOf(1f, 1f)

                // Send down, move, up
                val down = MotionEvents.sendDown(uiController, downCoordinates, precision).down
                MotionEvents.sendMovement(uiController, down, upCoordinates)
                MotionEvents.sendUp(uiController, down, upCoordinates)
            }

        }
    }

}