package com.github.pedramrn.slick.parent.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerListBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.state.ViewStateList;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerList extends FragmentBase implements ViewList, Retryable {

    @Inject
    Provider<PresenterList> provider;
    @Presenter
    PresenterList presenter;
    private ArrayList<ItemViewListParcelable> data;
    private String title;
    private GroupAdapter adapter;
    private UpdatingGroup adapterItems;
    private RecyclerView recyclerView;

    @Override
    public ArrayList<ItemViewListParcelable> data() {
        return data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getParcelableArrayList("DATA");
        title = getArguments().getString("TITLE");
    }

    public static ControllerList newInstance(@NonNull String title, @NonNull ArrayList<ItemViewListParcelable> itemViews) {

        Bundle bundle = new BundleBuilder(new Bundle())
                .putString("TITLE", title)
                .putParcelableArrayList("DATA", itemViews)
                .build();

        ControllerList fragment = new ControllerList();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        Navigator2.bind(this);
        App.componentMain().inject(this);
        PresenterList_Slick.bind(this);
        ControllerListBinding binding = ControllerListBinding.inflate(inflater, container, false);

        binding.toolbar.setTitle(title);
        setToolbar(binding.toolbar).setupButton(binding.toolbar, true);

        adapter = new GroupAdapter();
        adapterItems = new UpdatingGroup();

        adapter.setOnItemClickListener((item, view) -> {
            if (item instanceof OnItemAction) {
                ((OnItemAction) item).action(ControllerList.this, this, null, adapter.getAdapterPosition(item), view);
            }
        });

        // Section section = new Section(new ItemCardHeader(0, "All Casts"));
        // section.add(adapterItems);

        adapter.add(adapterItems);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private static final String TAG = ControllerList.class.getSimpleName();

    @Override
    public void update(ViewStateList state) {
        Log.d(TAG, "onNext() called with: state = [" + state + "]");
        adapterItems.update(state.items());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.setOnItemClickListener(null);
        recyclerView.setAdapter(null);
        recyclerView = null;
        adapterItems = null;
        adapter = null;
    }

    @Override
    public void onRetry(String tag) {
        retry.onNext(tag);
    }
}
