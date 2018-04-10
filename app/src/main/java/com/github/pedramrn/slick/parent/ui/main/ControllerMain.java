package com.github.pedramrn.slick.parent.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerMainBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.favorite.ControllerFavorite;
import com.github.pedramrn.slick.parent.ui.home.ControllerHome;
import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.middleware.RequestStack;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import static com.github.pedramrn.slick.parent.App.componentMain;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-02-13
 */

public class ControllerMain extends Controller implements ViewMain, BottomBarHost,
        BottomNavigation.OnMenuItemSelectionListener {

    private static final String TAG = ControllerMain.class.getSimpleName();
    @Inject
    Provider<PresenterMain> provider;
    @Presenter
    PresenterMain presenter;
    private ControllerMainBinding binding;
    private final RouterPagerAdapter routerPagerAdapter;
    private final int PAGE_COUNT = 3;

    protected ControllerMain(@Nullable Bundle args) {
        super(args);
        routerPagerAdapter = new RouterPagerAdapter(this) {

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public void configureRouter(@NonNull Router router, int position) {
                Log.d(TAG, "configureRouter() called with: router = [" + router + "], position = [" + position + "]");
                if (!router.hasRootController()) {
                    switch (position) {
                        case 0:
                            router.setRoot(RouterTransaction.with(new ControllerHome(args)));
                            break;
                        case 1:
                            router.setRoot(RouterTransaction.with(new ControllerBoxOffice()));
                            break;
                        case 2:
                            router.setRoot(RouterTransaction.with(new ControllerFavorite()));
                            break;
                    }
                }
            }
        };
        routerPagerAdapter.setMaxPagesToStateSave(3);
    }

    public ControllerMain(Uri data) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("URI", data)
                .build()
        );
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        componentMain().inject(this);
        PresenterMain_Slick.bind(this);
        binding = ControllerMainBinding.inflate(layoutInflater, viewGroup, false);
        binding.viewPager.setAdapter(routerPagerAdapter);
        binding.navigation.setOnMenuItemClickListener(this);
        binding.viewPager.setOffscreenPageLimit(2);
        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        binding.navigation.setOnMenuItemClickListener(null);
        binding = null;
        super.onDestroyView(view);
        Log.d(TAG, "onDestroyView() called");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
        App.disposeComponentMain();
        App.refWatcher(getActivity()).watch(this);
    }

    @Override
    public void onMenuItemSelect(@IdRes int itemId, int position, boolean fromUser) {
        if (fromUser) {
            resetStack(0);
            resetStack(1);
            resetStack(2);
            RequestStack.getInstance().clear();
            binding.viewPager.setCurrentItem(position, false);
            switch (itemId) {
                case R.id.navigation_box_office:
                    break;
                case R.id.navigation_home:
                    break;
                case R.id.navigation_favorite:
                    break;
            }
        }

    }

    private void resetStack(int position) {
        Router router = routerPagerAdapter.getRouter(position);
        if (router != null) {
            List<RouterTransaction> backstack = router.getBackstack();
            if (backstack.size() > 1) {
                backstack = backstack.subList(0, 1);
                router.setBackstack(backstack, new SimpleSwapChangeHandler());
            }
        }
    }

    @Override
    public void onMenuItemReselect(@IdRes int itemId, int position, boolean fromUser) {
        resetStack(position);
    }

    @Override
    public void show() {
        BottomNavigation navigation = binding.navigation;
        // if (!navigation.isExpanded()) {
        navigation.setExpanded(true, true);
        // }
    }

    @Override
    public void hide() {
        BottomNavigation navigation = binding.navigation;
        // if (navigation.isExpanded()) {
        navigation.setExpanded(false, true);
        // }
    }
}
