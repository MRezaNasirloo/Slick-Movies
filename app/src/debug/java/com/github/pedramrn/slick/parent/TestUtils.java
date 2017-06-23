package com.github.pedramrn.slick.parent;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okio.BufferedSource;
import okio.Okio;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-23
 */

public class TestUtils {

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string content
     */
    @NonNull
    public static String readFile(@NonNull final InputStream stream) throws IOException {
        try (final BufferedSource source = Okio.buffer(Okio.source(stream))) {
            return source.readUtf8();
        }
    }


    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string content
     */
    public static String readJson(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String str;
            while ((str = bf.readLine()) != null) {
                sb.append(str);
            }
        } finally {
            stream.close();
        }
        return sb.toString();
    }


}
