package com.github.pedramrn.slick.parent.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerListBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
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
public class ControllerList extends ControllerBase implements ViewList {

    @Inject
    Provider<PresenterList> provider;
    @Presenter
    PresenterList presenter;
    private final ArrayList<ItemViewListParcelable> data;
    private final String title;
    private GroupAdapter adapter;
    private UpdatingGroup adapterItems;
    private RecyclerView recyclerView;

    @Override
    public ArrayList<ItemViewListParcelable> data() {
        return data;
    }

    public ControllerList(@NonNull Bundle args) {
        super(args);
        data =  args.getParcelableArrayList("DATA");
        title = args.getString("TITLE");
    }

    public ControllerList(@NonNull String title, @NonNull ArrayList<ItemViewListParcelable> itemViews) {
        this(new BundleBuilder(new Bundle())
                .putString("TITLE", title)
                .putParcelableArrayList("DATA", itemViews)
                .build()
        );
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
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
                ((OnItemAction) item).action(ControllerList.this, null, adapter.getAdapterPosition(item));
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

    public static void start(Router router, String title, ArrayList<ItemViewListParcelable> itemViews) {
        router.pushController(RouterTransaction.with(new ControllerList(title, itemViews))
                                      .pushChangeHandler(new HorizontalChangeHandler())
                                      .popChangeHandler(new HorizontalChangeHandler()));
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        adapter.setOnItemClickListener(null);
        recyclerView.setAdapter(null);
        recyclerView = null;
        adapterItems = null;
        adapter = null;
    }
}
