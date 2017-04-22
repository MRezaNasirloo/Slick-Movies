package com.github.pedramrn.slick.parent.datasource.network;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

@GsonTypeAdapterFactory
public abstract class TypeAdapterFactoryGson implements TypeAdapterFactory {

    // Static factory method to access the package
    // private generated implementation
    public static TypeAdapterFactory create() {
        return new AutoValueGson_TypeAdapterFactoryGson();
    }

}
