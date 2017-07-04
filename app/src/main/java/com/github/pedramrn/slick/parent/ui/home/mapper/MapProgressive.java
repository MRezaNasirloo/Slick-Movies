package com.github.pedramrn.slick.parent.ui.home.mapper;

import android.support.v4.util.SparseArrayCompat;

import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */
public class MapProgressive implements Function<AutoBase, AutoBase> {

    private final int size;
    private final SparseArrayCompat<Integer> models;

    public MapProgressive(int size) {
        this.size = size;
        models = new SparseArrayCompat<>(size);
    }

    public MapProgressive() {
        this.size = Short.MAX_VALUE;
        models = new SparseArrayCompat<>(15);
    }

    @Override
    public AutoBase apply(@NonNull AutoBase model) throws Exception {
        int size = models.size();
        Integer id = models.get(model.uniqueId(), -1);
        if (id != -1) {
            return model.toBuilder().uniqueId(id).build();
        } else if (size < this.size) {
            models.put(model.uniqueId(), size);
            return model.toBuilder().uniqueId(size).build();
        } else {
            return model;
        }
    }
}
