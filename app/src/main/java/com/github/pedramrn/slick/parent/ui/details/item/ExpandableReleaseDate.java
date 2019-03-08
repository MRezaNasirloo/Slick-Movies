package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;

public class ExpandableReleaseDate implements ExpandableItem {

    private ExpandableGroup onToggleListener;

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        this.onToggleListener = onToggleListener;
    }
}
