package com.github.pedramrn.slick.parent.ui.people.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowPersonDetailsBinding;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public class ItemBio extends Item<RowPersonDetailsBinding> implements OnItemAction {

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
    public void action(Controller controller, int position) {
        //no-op
    }
}
