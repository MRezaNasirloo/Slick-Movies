package com.github.pedramrn.slick.parent.ui.custom;

import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.xwray.groupie.Group;
import com.xwray.groupie.Item;
import com.xwray.groupie.NestedGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A group which accepts a list of anticipated and diffs them against its previous contents,
 * generating the correct remove, add, move and change notifications to its parent observer,
 * to create an animated item-level update.
 * <p>
 * Item comparisons are made using:
 * - Item.getId() (are anticipated the same?)
 * - Item.equals() (are contents the same?)
 * If you don't customize getId() or equals(), the default implementations will return false,
 * meaning your Group will consider every update a complete change of everything.
 */
public class UpdatingGroupHash extends NestedGroup {

    private ListUpdateCallback listUpdateCallback = new ListUpdateCallback() {
        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            notifyItemRangeChanged(position, count);
        }
    };

    private List<Item> items = new ArrayList<>();

    public void update(List<? extends Item> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UpdatingCallback(newItems));
        super.removeAll(items);
        items.clear();
        super.addAll(newItems);
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(listUpdateCallback);
    }

    @Override
    public Group getGroup(int position) {
        return items.get(position);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getPosition(Group group) {
        if (group instanceof Item) {
            return items.indexOf(group);
        } else {
            return -1;
        }
    }

    private class UpdatingCallback extends DiffUtil.Callback {

        private List<? extends Item> newList;

        UpdatingCallback(List<? extends Item> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return items.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Item oldItem = items.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);
            if (oldItem.getLayout() != newItem.getLayout()) {
                return false;
            }
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Item oldItem = items.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);
            return oldItem.hashCode() == newItem.hashCode();
        }
    }
}

