package com.github.pedramrn.slick.parent.ui.image;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerImageBinding;
import com.github.pedramrn.slick.parent.ui.ActivityMain;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.github.pedramrn.slick.parent.ui.main.ViewPager;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerImage extends FragmentBase implements ViewImage {
    private static final String TAG = ControllerImage.class.getSimpleName();
    @Inject
    Provider<PresenterImage> provider;
    @Presenter
    PresenterImage presenter;
    private ArrayList<String> data;
    private String title;
    private int pos;
    private GroupAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int newPos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getStringArrayList("DATA");
        title = getArguments().getString("TITLE");
        pos = getArguments().getInt("POS", 0);
    }

    public static ControllerImage newInstance(@NonNull String title, @NonNull ArrayList<String> items, int position) {
        Bundle bundle = new BundleBuilder(new Bundle())
                .putStringArrayList("DATA", items)
                .putString("TITLE", title)
                .putInt("POS", position)
                .build();

        ControllerImage fragment = new ControllerImage();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ControllerImage newInstance(@NonNull String title, @NonNull ArrayList<String> items) {
        Bundle args = new BundleBuilder(new Bundle())
                .putStringArrayList("DATA", items)
                .putString("TITLE", title)
                .build();

        ControllerImage fragment = new ControllerImage();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        Navigator2.bind(this);
        App.componentMain().inject(this);
        PresenterImage_Slick.bind(this);
        ControllerImageBinding binding = ControllerImageBinding.inflate(inflater, container, false);
        adapter = new GroupAdapter();
        for (String url : data) {
            adapter.add(new ItemImage(url));
        }

        RecyclerView recyclerView = binding.recyclerView;
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        Fragment parent = getMainParent();
        ((BottomBarHost) parent).hide();
        newPos = pos;

        return binding.getRoot();
    }

    protected Fragment getMainParent() {
        return getActivity().getSupportFragmentManager().findFragmentByTag(ActivityMain.CONTROLLER_MAIN_TAG);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("POS", layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) return;
        newPos = savedInstanceState.getInt("POS", this.pos);
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutManager.scrollToPosition(newPos);
        toggleHideBar();
        // TODO: 2017-09-06 maybe adding a footer?
        View parentView = getMainParent().getView();
        ViewPager viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        viewPager.setLayoutParams(layoutParams);
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    public void onPause() {
        super.onPause();
        toggleHideBar();
        View parentView = getMainParent().getView();
        ViewPager viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, getActionBarHeight());
        viewPager.setLayoutParams(layoutParams);
    }

    @SuppressWarnings("ConstantConditions")
    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return getResources().getDimensionPixelOffset(R.dimen.action_bar_size);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((BottomBarHost) getMainParent()).show();
    }

    public void toggleHideBar() {

        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not boxOffice cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getActivity().getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
