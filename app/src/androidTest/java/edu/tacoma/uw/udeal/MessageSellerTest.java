package edu.tacoma.uw.udeal;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * This class is an Instrumentation test that handles the
 * testing for messaging a seller. This instrumentation
 * will select the first item in the list, message the seller
 * of that item, and then check if the message shows up
 * in the user's messages.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public class MessageSellerTest {

    /**
     * The activity test rule for main activity.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Instrumentation test that tests the send to seller functionality.
     */
    @Test
    public void sendSellerMessage() {
        // First scroll to the first item that needs to be clicked on
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        // Click on message seller fab button
        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(withId(R.id.fab))
                .perform(click());

        // Type message to seller and send.
        onView(withId(R.id.myMessageTextField))
                .perform(typeText("This is a test sending to seller"));
        onView(withId(R.id.sendButton))
                .perform(click());

        //Pressing back once only goes back once, so its in 3 times to make
        // sure it goes back to main before we can go to messages
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());

        // Check if message was sent
        // This test will pass only if the chat thread is at the top of your inbox. (position 0)
        // Depending on different user accounts, the position of the chat thread may be different
        onView(withId(R.id.nav_inbox))
                .perform(click());
        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(withId(R.id.fragment_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.inbox_detail_container))
                .check(matches(hasDescendant(withText("This is a test sending to seller"))));
    }

}
