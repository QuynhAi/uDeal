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


@RunWith(AndroidJUnit4.class)
public class MessageSellerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

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
