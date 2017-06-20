package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends ControllerBase implements ViewHome {

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GroupAdapter adapter = new GroupAdapter();
        binding.recyclerViewHome.setAdapter(adapter);
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        adapter.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        setToolbar(binding.toolbar);
        return binding.getRoot();
    }
}
