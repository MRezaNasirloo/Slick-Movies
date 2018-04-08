package com.github.pedramrn.slick.parent.ui.people.state;

import com.github.pedramrn.slick.parent.ui.people.item.ItemBio;
import com.github.pedramrn.slick.parent.ui.people.item.ItemMovieCast;
import com.github.pedramrn.slick.parent.ui.people.item.ItemMovieCrew;
import com.github.pedramrn.slick.parent.ui.people.item.ItemTvShowCast;
import com.github.pedramrn.slick.parent.ui.people.item.ItemTvShowCrew;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public class ViewStatePeoplePartials {

    public static class Person implements PartialViewState<ViewStatePeople> {

        private final PersonDetails personDetails;

        public Person(PersonDetails personDetails) {
            this.personDetails = personDetails;
        }

        @Override
        public ViewStatePeople reduce(ViewStatePeople state) {
            if (state.personDetails() != null) return state;
            return state.toBuilder().personDetails(personDetails).itemBio(new ItemBio(personDetails)).build();
        }
    }

    public static class PersonCredits implements PartialViewState<ViewStatePeople> {

        private final PersonDetails personDetails;

        public PersonCredits(PersonDetails personDetails) {
            this.personDetails = personDetails;
        }

        @Override
        public ViewStatePeople reduce(ViewStatePeople state) {
            PersonDetails ps = state.personDetails();
            ViewStatePeople.Builder builder = state.toBuilder();
            if (ps != null) {
                ps = personDetails.toBuilder().images(ps.images()).build();
            } else {
                ps = personDetails;
            }
            builder.personDetails(ps);
            builder.moviesCast(map(ps.movieCast(), new ItemCreditProducer() {
                @Override
                public Item produce(CastOrCrewPersonDetails coc) {
                    return new ItemMovieCast(coc);
                }
            }));
            builder.moviesCrew(map(ps.movieCrew(), new ItemCreditProducer() {
                @Override
                public Item produce(CastOrCrewPersonDetails coc) {
                    return new ItemMovieCrew(coc);
                }
            }));
            builder.tvShowsCast(map(ps.tvCast(), new ItemCreditProducer() {
                @Override
                public Item produce(CastOrCrewPersonDetails coc) {
                    return new ItemTvShowCast(coc);
                }
            }));
            builder.tvShowsCrew(map(ps.tvCrew(), new ItemCreditProducer() {
                @Override
                public Item produce(CastOrCrewPersonDetails coc) {
                    return new ItemTvShowCrew(coc);
                }
            }));
            return builder.build();
        }

        interface ItemCreditProducer {
            Item produce(CastOrCrewPersonDetails coc);
        }

        public List<Item> map(List<CastOrCrewPersonDetails> cocList, ItemCreditProducer producer) {
            ArrayList<Item> moviesCast = new ArrayList<>(cocList.size());
            for (CastOrCrewPersonDetails coc : cocList) {
                moviesCast.add(producer.produce(coc));
            }
            return moviesCast;
        }
    }

    public static class ErrorPerson implements PartialViewState<ViewStatePeople> {

        private final Throwable throwable;

        public ErrorPerson(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStatePeople reduce(ViewStatePeople state) {
            return state.toBuilder().errorPersonDetails(throwable).build();
        }
    }
}
