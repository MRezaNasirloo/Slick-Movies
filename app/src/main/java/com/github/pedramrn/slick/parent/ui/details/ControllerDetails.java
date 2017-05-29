package com.github.pedramrn.slick.parent.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.squareup.picasso.Picasso;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-28
 */

public class ControllerDetails extends Controller {

    private int pos;
    private MovieItem movieItem;

    public ControllerDetails(MovieItem movieItem, int position) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movieItem)
                .putInt("POS", position).build());
    }

    public ControllerDetails(@Nullable Bundle args) {
        super(args);
        pos = getArgs().getInt("POS");
        movieItem = getArgs().getParcelable("ITEM");
    }

    private static final String TAG = ControllerDetails.class.getSimpleName();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        ControllerDetailsBinding binding = ControllerDetailsBinding.inflate(inflater, container, false);
        String transitionName = getResources().getString(R.string.transition_poster, pos);
        binding.imageViewHeader.setTransitionName(transitionName);
        Picasso.with(getApplicationContext()).load(movieItem.poster()).into(binding.imageViewHeader);

        return binding.getRoot();
    }
}
