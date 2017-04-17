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
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImpl;
import com.github.pedramrn.slick.parent.ui.popular.ControllerPopular;
import com.github.pedramrn.slick.parent.ui.upcoming.ControllerUpComing;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.CompositeDisposable;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-13
 */

public class ControllerMain extends Controller implements ViewMain, BottomNavigation.OnMenuItemSelectionListener {

    private static final String TAG = ControllerMain.class.getSimpleName();
    @Inject
    RouterBoxOfficeImpl routerBoxOffice;

    @Inject
    Provider<PresenterMain> provider;
    @Presenter
    PresenterMain presenter;
    CompositeDisposable disposable = new CompositeDisposable();
    private ControllerHomeBinding binding;
    private final RouterPagerAdapter routerPagerAdapter;

    public ControllerMain() {
        routerPagerAdapter = new RouterPagerAdapter(this) {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public void configureRouter(Router router, int position) {
                if (!router.hasRootController()) {
                    switch (position) {
                        case 0:
                            router.setRoot(RouterTransaction.with(new ControllerBoxOffice()));
                            break;
                        case 1:
                            router.setRoot(RouterTransaction.with(new ControllerUpComing()));
                            break;
                        case 2:
                            router.setRoot(RouterTransaction.with(new ControllerPopular()));
                            break;
                    }
                }
            }
        };
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        App.getMainComponent(getRouter()).inject(this);
        ControllerMain_Slick.bind(this);
        binding = ControllerHomeBinding.inflate(layoutInflater, viewGroup, false);
        binding.viewPager.setAdapter(routerPagerAdapter);
        binding.navigation.setOnMenuItemClickListener(this);
        return binding.getRoot();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.disposeMainComponent();
    }

    @Override
    public void onMenuItemSelect(@IdRes int itemId, int position, boolean fromUser) {
        Log.e(TAG, "onMenuItemSelect() called with: itemId = [" + itemId + "], position = [" + position + "], fromUser = [" + fromUser + "]");
        if (fromUser) {
            binding.viewPager.setCurrentItem(position, false);
            switch (itemId) {
                case R.id.navigation_box_office:
                    break;
                case R.id.navigation_dashboard:
                    break;
                case R.id.navigation_notifications:
                    break;
            }
        }

    }

    @Override
    public void onMenuItemReselect(@IdRes int itemId, int position, boolean fromUser) {
        Log.e(TAG, "onMenuItemReselect() called with: itemId = [" + itemId + "], position = [" + position + "], fromUser = [" + fromUser + "]");
    }
}
