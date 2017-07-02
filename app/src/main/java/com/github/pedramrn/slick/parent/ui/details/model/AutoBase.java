package com.github.pedramrn.slick.parent.ui.details.model;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */

public abstract class AutoBase {
    public abstract Integer uniqueId();

    public abstract BuilderBase toBuilder();

    public static abstract class BuilderBase {
        public abstract BuilderBase uniqueId(Integer id);

        public abstract AutoBase build();
    }
}
