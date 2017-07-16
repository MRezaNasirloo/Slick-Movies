package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.utils.ConductorTestRule;
import com.github.pedramrn.slick.parent.utils.di.TestApp;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.pedramrn.slick.parent.util.FileUtils.readFile;
import static com.github.pedramrn.slick.parent.utils.recyclerview.RecyclerViewItemCountAssertion.withItemCount;
import static com.github.pedramrn.slick.parent.utils.recyclerview.TestUtils.withRecyclerView;
import static javax.net.ssl.HttpsURLConnection.HTTP_OK;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-23
 */
@RunWith(AndroidJUnit4.class)
public class ControllerBoxOfficeTest {

    @Rule
    public ConductorTestRule<ControllerBoxOffice> rule = new ConductorTestRule<>(new ControllerBoxOffice());
    private MockWebServer server;
    private OkHttp3IdlingResource resource;
    private OkHttp3IdlingResource okHttp;

    @Before
    public void setup() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        InputStream isTmdb = context.getAssets().open("api_tmdb_simple.json");
        InputStream isTrakt = context.getAssets().open("api_trakt.json");
        final String bufferTmdb = readFile(isTmdb);
        final String bufferTrakt = readFile(isTrakt);

        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                String path = request.getPath();
                if (path.startsWith("/movies/boxoffice")) {
                    return new MockResponse()
                            .setResponseCode(HTTP_OK)
                            .setBody(bufferTrakt);
                } else if (path.startsWith("/movie/")) {
                    return new MockResponse()
                            .setResponseCode(HTTP_OK)
                            .setBody(bufferTmdb);
                } else {
                    throw new IllegalStateException(path + "is not supported.");
                }
            }
        });
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                server.start();
                TestApp.setBaseUrl(server.url("/"));
            }
        }).subscribeOn(Schedulers.io()).subscribe();

        TestApp.setOkHttpClient(new ModuleNetwork().okHttpClient(Collections.<Interceptor>emptyList(), ));
        okHttp = OkHttp3IdlingResource.create("OkHttp", TestApp.getOkHttpClient());
        Espresso.registerIdlingResources(okHttp);
    }

    private static final String TAG = ControllerBoxOfficeTest.class.getSimpleName();

    @After
    public void tearDown() throws Exception {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                server.shutdown();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
        Espresso.unregisterIdlingResources(okHttp);

    }

    @Test
    public void test_controller_box_office() throws Exception {

        rule.launchActivity(new Intent());


        Matcher<View> recyclerView = withId(R.id.recycler_view);
        onView(recyclerView).perform(swipeUp());

        onView(recyclerView).perform(scrollToPosition(0));


        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_title))
                .check(matches(withText("Interstellar")));

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_rank))
                .check(matches(withText("#1")));


        onView(recyclerView).perform(scrollToPosition(9)).perform();

        onView(recyclerView).check(withItemCount(10));

    }

}