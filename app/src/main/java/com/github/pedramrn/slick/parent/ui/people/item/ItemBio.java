package com.github.pedramrn.slick.parent.ui.people.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowPersonDetailsBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.xwray.groupie.databinding.BindableItem;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public class ItemBio extends BindableItem<RowPersonDetailsBinding> implements OnItemAction {

    private final PersonDetails personDetails;

    public ItemBio(PersonDetails personDetails) {
        super(0);
        this.personDetails = personDetails;
    }

    @Override
    public int getLayout() {
        return R.layout.row_person_details;
    }

    @Override
    public void bind(RowPersonDetailsBinding viewBinding, int position) {
        if (personDetails != null) {
            String text = personDetails.biography();
            String biography = text == null || text.isEmpty() ? viewBinding.getRoot().getContext().getString(R.string.bio_empty) : text;
            viewBinding.textViewBio.setText(biography);
        }
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }
}
