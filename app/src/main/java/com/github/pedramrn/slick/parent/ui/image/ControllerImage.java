package com.github.pedramrn.slick.parent.ui.image;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerImageBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.github.pedramrn.slick.parent.ui.main.ViewPager;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerImage extends ControllerElm<ViewStateImage> implements ViewImage {

    @Inject
    Provider<PresenterImage> provider;
    @Presenter
    PresenterImage presenter;
    private final ArrayList<String> data;
    private final String title;
    private GroupAdapter adapter;

    public ControllerImage(@NonNull Bundle args) {
        super(args);
        data = args.getStringArrayList("DATA");
        title = args.getString("TITLE");
    }

    public ControllerImage(@NonNull String title, @NonNull ArrayList<String> items) {
        this(new BundleBuilder(new Bundle())
                .putStringArrayList("DATA", items)
                .putString("TITLE", title)
                .build());
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: 2017-07-22 Inject dependencies 
        App.componentMain().inject(this);
        ControllerImage_Slick.bind(this);
        ControllerImageBinding binding = ControllerImageBinding.inflate(inflater, container, false);
        adapter = new GroupAdapter();
        for (String url : data) {
            adapter.add(new ItemImage(url));
        }

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        presenter.updateStream().subscribe(this);
        return binding.getRoot();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        toggleHideyBar();
        // TODO: 2017-09-06 maybe adding a footer?
        ((BottomBarHost) getParentController()).hide();
        View parentView = getParentController().getView();
        ViewPager viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        viewPager.setLayoutParams(layoutParams);
    }


    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        toggleHideyBar();
        ((BottomBarHost) getParentController()).show();
        View parentView = getParentController().getView();
        int measuredHeight = parentView.findViewById(R.id.navigation).getMeasuredHeight();
        ViewPager viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, measuredHeight);
        viewPager.setLayoutParams(layoutParams);
    }

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
    }

    @Override
    public void onNext(ViewStateImage state) {
        // TODO: 2017-09-06
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    public void toggleHideyBar() {

        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
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
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getActivity().getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private static final String TAG = ControllerImage.class.getSimpleName();
}
