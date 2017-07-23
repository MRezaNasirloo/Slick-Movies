package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
class TypeAdapterIdsToString extends TypeAdapter<String> {

    private final TypeAdapter<String> slugAdapter;

    public TypeAdapterIdsToString() {
        slugAdapter = TypeAdapters.STRING;
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("slug");
        slugAdapter.write(out, value);
        out.endObject();
    }

    @Override
    public String read(JsonReader in) throws IOException {
        String slug = null;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        if (in.hasNext()) {
            in.beginObject();
            while (in.hasNext()) {
                String _name = in.nextName();
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    continue;
                }
                switch (_name) {
                    case "slug": {
                        slug = slugAdapter.read(in);
                        break;
                    }
                    default: {
                        in.skipValue();
                    }
                }
            }
        }
        in.endObject();
        return slug;
    }
}
