package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.utils.ConductorTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.pedramrn.slick.parent.utils.recyclerview.RecyclerViewItemCountAssertion.withItemCount;
import static com.github.pedramrn.slick.parent.utils.recyclerview.TestUtils.withRecyclerView;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-23
 */
@RunWith(AndroidJUnit4.class)
public class ControllerBoxOfficeTest {

    @Rule
    public ConductorTestRule<ControllerBoxOffice> rule = new ConductorTestRule<>(new ControllerBoxOffice());

    @Test
    public void useAppContext() throws Exception {

        //        rule.launchActivity(null);

        Matcher<View> recyclerView = withId(R.id.recycler_view);
        onView(recyclerView).check(withItemCount(8));


        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_title))
                .check(matches(withText("Beauty and the Beast")));

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_rank))
                .check(matches(withText("#1")));

        onView(recyclerView).perform(scrollToPosition(5)).perform(click());


        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(5, R.id.textView_rank))
                .check(matches(withText("#6")));

        onView(recyclerView).perform(scrollToPosition(7)).perform(click());

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(7, R.id.textView_title))
                .check(matches(withText("Beauty and the Beast")));

        //checks more items loaded at the end of list
        onView(recyclerView).check(withItemCount(10));

        onView(recyclerView).perform(scrollToPosition(9)).perform(click());

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(9, R.id.textView_rank))
                .check(matches(withText("#10")));
    }

}