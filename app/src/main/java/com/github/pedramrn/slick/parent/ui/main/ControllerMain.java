package com.github.pedramrn.slick.parent.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerMainBinding;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.middleware.RequestStack;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import static com.github.pedramrn.slick.parent.App.componentMain;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-02-13
 */

public class ControllerMain extends FragmentBase implements ViewMain, ViewIran, BottomBarHost,
        BottomNavigation.OnMenuItemSelectionListener {

    private static final String TAG = ControllerMain.class.getSimpleName();
    private FragmentStatePagerAdapter pagerAdapter;
    @Inject
    Provider<PresenterMain> provider;
    @Presenter
    PresenterMain presenter;
    @Inject
    Provider<PresenterIran> providerIran;
    @Presenter
    PresenterIran presenterIran;
    private ControllerMainBinding binding;

    public static ControllerMain newInstance(Bundle args) {
        ControllerMain fragment = new ControllerMain();
        fragment.setArguments(args);
        return fragment;
    }

    public static ControllerMain newInstance(Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("URI", uri);
        ControllerMain fragment = new ControllerMain();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        componentMain().inject(this);
        PresenterMain_Slick.bind(this);
        PresenterIran_Slick.bind(this);
        binding = ControllerMainBinding.inflate(inflater, container, false);
        pagerAdapter = new SectionPagerAdapter();
        binding.viewPager.setAdapter(pagerAdapter);
        binding.navigation.setOnMenuItemClickListener(this);
        binding.viewPager.setOffscreenPageLimit(2);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding.navigation.setOnMenuItemClickListener(null);
        binding = null;
        pagerAdapter = null;
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() called");
    }

    @Override
    public void onDestroy() {
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
        // Fragment item = pagerAdapter.getItem(position);
        // TODO: 2018-04-25 Reset back stack for this fragment
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

    public boolean onBackPressed() {
        return BackStackFragment.handleBackPressed(getChildFragmentManager());
    }

    @Override
    public void showWarningIran() {
        showSnakbar(getResources().getString(R.string.message_iran));
    }

    @NonNull
    @Override
    protected String getSnackbarActionText() {
        return getResources().getString(R.string.ok);
    }

    @NonNull
    @Override
    public Observable<Object> errorDismissed() {
        return retry;
    }


    private class SectionPagerAdapter extends FragmentStatePagerAdapter {
        private final int PAGE_COUNT = 3;
        private final SparseArray<Fragment> fragments = new SparseArray<>(3);

        SectionPagerAdapter() {
            super(getChildFragmentManager());
            fragments.put(0, FragmentContainer.newInstance(getArguments()));
            fragments.put(1, FragmentContainerBoxOffice.newInstance());
            fragments.put(2, FragmentContainerFavorite.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
