package com.github.pedramrn.slick.parent.ui.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
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
import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.home.ControllerHome;
import com.github.pedramrn.slick.parent.ui.popular.ControllerPopular;
import com.github.slick.Presenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.CompositeDisposable;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import static com.github.pedramrn.slick.parent.App.componentMain;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-13
 */

public class ControllerMain extends Controller implements ViewMain, BottomBarHost, BottomNavigation.OnMenuItemSelectionListener {

    private static final String TAG = ControllerMain.class.getSimpleName();
    @Inject
    Provider<PresenterMain> provider;
    @Presenter
    PresenterMain presenter;
    CompositeDisposable disposable = new CompositeDisposable();
    private ControllerMainBinding binding;
    private final RouterPagerAdapter routerPagerAdapter;
    private final int pageCount = 3;

    public ControllerMain() {
        routerPagerAdapter = new RouterPagerAdapter(this) {

            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public void configureRouter(@NonNull Router router, int position) {
                Log.d(TAG, "configureRouter() called with: router = [" + router + "], position = [" + position + "]");
                if (!router.hasRootController()) {
                    switch (position) {
                        case 0:
                            router.setRoot(RouterTransaction.with(new ControllerHome()));
                            break;
                        case 1:
                            router.setRoot(RouterTransaction.with(new ControllerBoxOffice()));
                            // router.setRoot(RouterTransaction.with(new ControllerUpComing()));
                            break;
                        case 2:
                            router.setRoot(RouterTransaction.with(new ControllerPopular()));
                            break;
                    }
                }
            }
        };
        routerPagerAdapter.setMaxPagesToStateSave(3);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        componentMain().inject(this);
        ControllerMain_Slick.bind(this);
        binding = ControllerMainBinding.inflate(layoutInflater, viewGroup, false);
        binding.viewPager.setAdapter(routerPagerAdapter);
        binding.navigation.setOnMenuItemClickListener(this);
        binding.viewPager.setOffscreenPageLimit(2);
        /*AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(getActivity(), R.menu.navigation);
        navigationAdapter.setupWithBottomNavigation(binding.bottomNavigation, new int[]{Color.CYAN,Color.GRAY,Color.DKGRAY});
        binding.bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                binding.viewPager.setCurrentItem(position, false);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                return true;
            }
        });*/
        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        binding.navigation.setOnMenuItemClickListener(null);
        super.onDestroyView(view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.disposeComponentMain();
    }

    @Override
    public void onMenuItemSelect(@IdRes int itemId, int position, boolean fromUser) {
        if (fromUser) {
            resetStack(position);
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
        if (!navigation.isExpanded()) {
            navigation.setExpanded(true, true);
        }
    }

    @Override
    public void hide() {
        BottomNavigation navigation = binding.navigation;
        if (navigation.isExpanded()) {
            navigation.setExpanded(false, true);
        }

    }
}
