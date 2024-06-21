package com.foundie.id.login

import com.foundie.id.ui.navigation.FragmentActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.foundie.id.R
import com.foundie.id.ui.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var activityScenario: ActivityScenario<LoginActivity>

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
        activityScenario = activityRule.scenario
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginAndLogout() {
        onView(withId(R.id.et_email_login)).perform(typeText("lostvape01@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password_login)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))

        ActivityScenario.launch(FragmentActivity::class.java)

        onView(withId(R.id.profile)).perform(click())

        onView(withId(R.id.menu_settings)).perform(click())
        onView(withId(R.id.tv_logout)).perform(click())

        onView(withText(R.string.LOGOUT_CONFIRMATION_TITLE)).check(matches(isDisplayed()))
        onView(withText(R.string.LOGOUT_CONFIRMATION_MESSAGE)).check(matches(isDisplayed()))
        onView(withText(R.string.LOGOUT_CONFIRMATION_YES)).check(matches(isDisplayed()))
        onView(withText(R.string.LOGOUT_CONFIRMATION_YES)).perform(click())
    }
}
