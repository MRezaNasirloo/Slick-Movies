package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;

import com.github.pedramrn.slick.parent.databinding.RowCastListBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-21
 */

public class ItemListHorizontalPager extends ItemListHorizontal {
//    private final SnapHelper snapHelper = new PagerSnapHelper();

    public ItemListHorizontalPager(Context context, GroupAdapter adapter, String tag) {
        super(adapter, tag);
    }

    @Override
    public void bind(RowCastListBinding viewBinding, int position) {
        super.bind(viewBinding, position);
//        snapHelper.attachToRecyclerView(viewBinding.recyclerViewCasts);
    }

    @Override
    public void unbind(ViewHolder<RowCastListBinding> holder) {
//        holder.binding.recyclerViewCasts.setOnFlingListener(null);
        super.unbind(holder);
    }
}
