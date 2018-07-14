package com.github.pedramrn.slick.parent.ui.home;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.Screen;
import com.github.pedramrn.slick.parent.ui.ScreenTransition;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList_Slick;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.PresenterSearch_Slick;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends FragmentBase implements ViewHome, Navigator {

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;

    private UpdatingGroup progressiveUpcoming;

    private SearchViewImpl searchView;

    private PublishSubject<String> onRetry = PublishSubject.create();

    private static final String trending = "Trending";
    private static final String popular = "Popular";
    private static final String upcoming = "Upcoming";
    private RecyclerViewCardListTrending recyclerViewCardListTrending;
    private RecyclerViewCardListPopular recyclerViewCardListPopular;
    private GroupAdapter adapterUpcoming;
    private RecyclerView recyclerViewUpcoming;
    private ScrollingPagerIndicator pageIndicator;
    private boolean handled;
    private Uri uri;

    public static ControllerHome newInstance(Bundle bundle) {
        Bundle args = new Bundle();
        ControllerHome fragment = new ControllerHome();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) handled = savedInstanceState.getBoolean("HANDLED", false);
        Bundle bundle = getArguments();
        if (bundle != null) { uri = bundle.getParcelable("URI"); } else uri = null;
        Log.i(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.i(TAG, "onCreateView");
        App.componentMain().inject(this);
        PresenterHome_Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        adapterUpcoming = new GroupAdapter();
        progressiveUpcoming = new UpdatingGroup();

        final Context context = getContext().getApplicationContext();

        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.row_card, 12);
        recyclerViewCardListTrending = binding.recyclerViewTrending;
        recyclerViewCardListPopular = binding.recyclerViewPopular;

        String instanceId = getInstanceId();
        recyclerViewCardListPopular.onBind(instanceId);
        recyclerViewCardListTrending.onBind(instanceId + 1);

        recyclerViewCardListTrending.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListPopular.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListTrending.setNavigator(this);
        recyclerViewCardListPopular.setNavigator(this);
        recyclerViewCardListPopular.setRecycledViewPool(recycledViewPool);
        recyclerViewCardListTrending.setRecycledViewPool(recycledViewPool);

        boolean isRTL = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;
        Log.w(TAG, "Layout isRTL: " + isRTL);
        if (!isRTL) {
            SnapHelper snapHelperStartTrending = new GravitySnapHelper(Gravity.START);
            SnapHelper snapHelperStartPopular = new GravitySnapHelper(Gravity.START);
            snapHelperStartTrending.attachToRecyclerView(recyclerViewCardListTrending);
            snapHelperStartPopular.attachToRecyclerView(recyclerViewCardListPopular);
        }

        adapterUpcoming.add(progressiveUpcoming);


        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        searchView.onBind(instanceId);
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

        recyclerViewUpcoming = binding.recyclerViewUpcoming;
        recyclerViewUpcoming.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layout);
        recyclerViewUpcoming.setAdapter(adapterUpcoming);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerViewUpcoming);

        pageIndicator = binding.pageIndicator;
        pageIndicator.attachToRecyclerView(recyclerViewUpcoming);

        //setup headers
        setupHeader(binding.headerUpcoming, upcoming);
        setupHeader(binding.headerTrending, trending);
        setupHeader(binding.headerPopular, popular);

        handleIntent(uri);

        return binding.getRoot();
    }

    private void handleIntent(Uri uri) {
        Log.d(TAG, "handleIntent() called with: uri = [" + uri + "]");
        if (uri != null && !handled) {
            handled = true;
            List<String> pathSegments = uri.getPathSegments();
            String path = pathSegments.size() > 0 ? pathSegments.get(0) : null;
            if ("title".equalsIgnoreCase(path)) {
                if (pathSegments.size() > 1) {
                    String imdbId = pathSegments.get(1);
                    Screen screen = ControllerDetails.newInstance(imdbId, null);
                    navigateTo(screen);
                }
            }
            else if ("name".equalsIgnoreCase(path)) {
                // TODO: 2018-04-09 Launch Controller People
            }
        }
    }

    private void setupHeader(RowCardHeaderBinding header, String title, @Nullable Consumer<Object> clickListener) {
        if (clickListener == null) {
            header.button.setVisibility(View.GONE);
        } /*else {
            RxView.clicks(header.button).throttleLast(1, TimeUnit.SECONDS).subscribe(clickListener);
        }*/
        header.textViewTitle.setText(title);
    }

    private void setupHeader(RowCardHeaderBinding header, String title) {
        setupHeader(header, title, null);
    }

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView() called");
        super.onDestroyView();
        pageIndicator.detachFromPager();
        recyclerViewUpcoming.setAdapter(null);
        adapterUpcoming.setOnItemClickListener(null);
        recyclerViewCardListTrending.onDestroy();
        recyclerViewCardListPopular.onDestroy();
        recyclerViewCardListTrending = null;
        recyclerViewCardListPopular = null;
        recyclerViewUpcoming = null;
        progressiveUpcoming = null;
        adapterUpcoming = null;
        pageIndicator = null;
        searchView = null;
    }

    // TODO: 2018-04-25 add this
    /*@Override
    public boolean handleBack() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
            return true;
        }
        return super.handleBack();
    }*/

    @NonNull
    @Override
    public Observable<Object> retryUpcoming() {
        return onRetry.filter(PresenterHome.UPCOMING::equals).cast(Object.class);
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item).action(ControllerHome.this, this, null, adapter
                .getAdapterPosition(item), view));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity activity = getActivity();
        String instanceId = getInstanceId();
        PresenterCardList_Slick.onDestroy(instanceId, activity);
        PresenterCardList_Slick.onDestroy(instanceId + 1, activity);
        PresenterSearch_Slick.onDestroy(instanceId, activity);
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("HANDLED", handled);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @NonNull
    @Override
    public SnackbarManager snackbarManager() {
        throw new NotImplementedException("Sorry!!");
    }


    @Override
    public ScreenTransition getScreenTransition() {
        TransitionSet transition = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeClipBounds())
                .addTransition(new ChangeImageTransform());
        transition.setPathMotion(new ArcMotion());

        return new ScreenTransition() {
            @Override
            public Transition sharedElementEnterTransition() {
                return transition;
            }

            @Override
            public Transition sharedElementReturnTransition() {
                return transition;
            }

            @Override
            public Transition exitTransition() {
                return new Fade();
            }

            @Override
            public Transition enterTransition() {
                return new Fade();
            }

            @Override
            public Transition reenterTransition() {
                return new Fade();
            }
        };
    }

    @Override
    public void setScreenTransition(ScreenTransition screenTransition) {
        setExitTransition(screenTransition.exitTransition().setDuration(duration));
        setReenterTransition(screenTransition.reenterTransition().setDuration(duration));
        setEnterTransition(screenTransition.enterTransition().setDuration(duration));
        // setSharedElementEnterTransition(screenTransition.sharedElementEnterTransition());
        // setSharedElementReturnTransition(screenTransition.sharedElementReturnTransition());
    }
}
