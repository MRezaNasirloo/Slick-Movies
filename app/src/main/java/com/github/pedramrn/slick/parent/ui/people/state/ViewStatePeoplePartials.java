package com.github.pedramrn.slick.parent.ui.people.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;

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
            return state.toBuilder().personDetails(personDetails).build();
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
