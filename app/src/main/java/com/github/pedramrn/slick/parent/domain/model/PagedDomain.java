package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-18
 */
@AutoValue
public abstract class PagedDomain<T> {
    /**
     * @return the data in question
     */
    public abstract List<T> data();

    /**
     * @return current page
     */
    public abstract int page();

    /**
     * @return total page count
     */
    public abstract int pages();

    /**
     * @return total item count
     */
    public abstract int count();

    public static <T> PagedDomain<T> create(List<T> data, int page, int pages, int count) {
        return PagedDomain.<T>builder()
                .data(data)
                .page(page)
                .pages(pages)
                .count(count)
                .build();
    }

    public static <T> Builder<T> builder() {
        return new AutoValue_PagedDomain.Builder<>();
    }


    @AutoValue.Builder
    public abstract static class Builder<T> {
        public abstract Builder<T> data(List<T> data);

        public abstract Builder<T> page(int page);

        public abstract Builder<T> pages(int pages);

        public abstract Builder<T> count(int count);

        public abstract PagedDomain<T> build();
    }
}
