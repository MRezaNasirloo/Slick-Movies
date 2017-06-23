package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.utils.ConductorTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.Okio;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.net.HttpURLConnection.HTTP_NOT_IMPLEMENTED;
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

    @Before
    public void setup() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        InputStream isTmdb = context.getAssets().open("api_tmdb_simple.json");
        InputStream isTrakt = context.getAssets().open("api_trakt.json");
        server = new MockWebServer();
        final Buffer bufferTmdb = Okio.buffer(Okio.source(isTmdb)).buffer();
        final Buffer bufferTrakt = Okio.buffer(Okio.source(isTrakt)).buffer();
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
                    return new MockResponse()
                            .setResponseCode(HTTP_NOT_IMPLEMENTED)
                            .setBody("[]");
                }
            }
        });
        server.start();
        HttpUrl url = server.url("https://api.test.com/");
        Log.e(TAG, url.toString());
    }

    private static final String TAG = ControllerBoxOfficeTest.class.getSimpleName();

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    private String readJson(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String str;
            while ((str = bf.readLine()) != null) {
                sb.append(str);
            }
        }

        return sb.toString();
    }

    @Test
    public void test_controller_box_office() throws Exception {

        // rule.launchActivity(new Intent());


        Matcher<View> recyclerView = withId(R.id.recycler_view);
        onView(recyclerView).perform(swipeUp());
        onView(recyclerView).perform(swipeUp());
        onView(recyclerView).perform(swipeUp());

        /*onView(recyclerView).check(withItemCount(0));



        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_title))
                .check(matches(withText("Beauty and the Beast")));

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.textView_rank))
                .check(matches(withText("#1")));

        onView(recyclerView).perform(scrollToPosition(9)).perform();

        onView(recyclerView).perform(swipeUp());


        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(9, R.id.textView_rank))
                .check(matches(withText("#10")));*/
    }

}