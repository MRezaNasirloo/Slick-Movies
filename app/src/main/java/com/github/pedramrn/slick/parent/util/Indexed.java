package com.github.pedramrn.slick.parent.util;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class Indexed<T> {
    final int index;
    final T value;

    public Indexed(T value, int index) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return index + ") " + value;
    }
}
