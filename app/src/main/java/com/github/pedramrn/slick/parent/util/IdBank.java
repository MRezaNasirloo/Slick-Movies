package com.github.pedramrn.slick.parent.util;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-05
 */

public class IdBank {
    private static AtomicInteger id = new AtomicInteger(0);

    private static SparseArrayCompat<AtomicInteger> map = new SparseArrayCompat<>(5);

    public static int nextId(@NonNull String TAG) {
        if (map == null) map = new SparseArrayCompat<>(5);
        AtomicInteger idGen = map.get(TAG.hashCode());
        if (idGen == null) {
            idGen = new AtomicInteger(0);
            map.put(TAG.hashCode(), idGen);
        }
        return idGen.getAndIncrement();
    }

    public static int nextId() {
        if (id == null) id = new AtomicInteger(0);
        return id.getAndIncrement();
    }

    public static void disposeAll() {
        map.clear();
        map = null;
        id = null;
    }

    public static void dispose(String TAG) {
        map.remove(TAG.hashCode());
    }

    public static void reset() {
        if (id != null) {
            id.set(0);
        }
    }

    public static void reset(String TAG) {
        if (map != null) {
            AtomicInteger id = map.get(TAG.hashCode());
            if (id == null) {
                id = new AtomicInteger(0);
                map.put(TAG.hashCode(), id);
            } else {
                id.set(0);
            }
        }
    }

}
