package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.databinding.RowCastHorizontalBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.ControllerPeople;
import com.github.pedramrn.slick.parent.ui.people.model.Person;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemCast extends ItemCastProgressive implements OnItemAction {

    private final Cast cast;

    public ItemCast(int id, Cast cast) {
        super(id);
        this.cast = cast;
    }

    @Override
    public void bind(RowCastHorizontalBinding viewBinding, int position) {
        viewBinding.textViewCharacter.setBackground(null);
        viewBinding.textViewName.setBackground(null);
        viewBinding.imageViewProfile.load(cast.profileIcon());
        viewBinding.textViewName.setText(cast.name());
        viewBinding.textViewCharacter.setText(cast.character());
    }

    public Cast cast() {
        return cast;
    }

    public String transitionName() {
        return String.valueOf(cast.uniqueId() + getPosition(this));
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        navigator.getRouter().pushController(RouterTransaction.with(new ControllerPeople(Person.create(
                cast.id(),
                cast.name(),
                cast.profilePicId()
        ), transitionName()))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler()));
    }
}
