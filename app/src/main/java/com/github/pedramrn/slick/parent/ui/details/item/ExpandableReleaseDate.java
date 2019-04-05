package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.databinding.BindableItem;

public class ExpandableReleaseDate extends BindableItem<RowCardHeaderBinding> implements ExpandableItem, OnItemAction {

    private ExpandableGroup onToggleListener;

    public ExpandableReleaseDate() {
        super(90);
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        this.onToggleListener = onToggleListener;
    }

    @Override
    public void bind(@NonNull RowCardHeaderBinding viewBinding, int position) {
        viewBinding.button.setText("SHOW");
        viewBinding.button.setVisibility(View.VISIBLE);
        viewBinding.button.setOnClickListener(v -> {
            onToggleListener.onToggleExpanded();
            if (onToggleListener.isExpanded()) {
                viewBinding.button.setText("HIDE");
            } else {
                viewBinding.button.setText("SHOW");

            }
        });
        viewBinding.textViewTitle.setText("Release dates");
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_header;
    }

    @Override
    public void action(
            @NonNull Navigator navigator,
            Retryable retryable,
            @Nullable Object payload,
            int position,
            @NonNull View view
    ) {
        //no-op
    }
}