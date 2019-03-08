package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastHorizontalBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.ControllerPeople;
import com.github.pedramrn.slick.parent.ui.people.model.Person;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-16
 */

public class ItemCast extends ItemCastProgressive implements OnItemAction {

    private final Cast cast;
    private String transitionName;

    public ItemCast(int id, Cast cast) {
        super(id);
        this.cast = cast;
        transitionName = transitionName();
    }

    @Override
    public void bind(@NonNull RowCastHorizontalBinding viewBinding, int position) {
        viewBinding.textViewCharacter.setBackground(null);
        viewBinding.textViewName.setBackground(null);
        viewBinding.imageViewProfile.load(cast.profileIcon());
        viewBinding.imageViewProfile.setTransitionName(transitionName);
        viewBinding.textViewName.setText(cast.name());
        viewBinding.textViewCharacter.setText(cast.character());
    }

    public Cast cast() {
        return cast;
    }

    public String transitionName() {
        return String.valueOf("CAST_" + cast.uniqueId());
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        Person person = Person.create(
                cast.id(),
                cast.name(),
                cast.profilePicId()
        );
        View sharedView = view.findViewById(R.id.imageView_profile);
        navigator.navigateTo(ControllerPeople.newInstance(person, sharedView.getTransitionName()),
                sharedView, sharedView.getTransitionName());
    }
}
