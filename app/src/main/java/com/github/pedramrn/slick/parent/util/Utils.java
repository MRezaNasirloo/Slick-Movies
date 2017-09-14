package com.github.pedramrn.slick.parent.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.xwray.groupie.Item;

import java.util.Iterator;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isNetworkAvailable(ConnectivityManager cm) {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void removeRemovables(Iterator<Item> iterator) {
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (((RemovableOnError) item).removable()) {
                iterator.remove();
            }
        }
    }
}
