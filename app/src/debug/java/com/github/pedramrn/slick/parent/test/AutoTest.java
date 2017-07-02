package com.github.pedramrn.slick.parent.test;

import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */
@AutoValue
public abstract class AutoTest extends AutoBase {

    public abstract Builder toBuilder();

    public static AutoTest create(Integer uniqueId) {
        return builder()
                .uniqueId(uniqueId)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_AutoTest.Builder();
    }


    @AutoValue.Builder
    public static abstract class Builder extends BuilderBase {
        public abstract Builder uniqueId(Integer id);

        public abstract AutoTest build();

    }
}
