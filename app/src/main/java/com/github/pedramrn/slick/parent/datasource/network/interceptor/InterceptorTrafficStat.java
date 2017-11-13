package com.github.pedramrn.slick.parent.datasource.network.interceptor;

import android.net.TrafficStats;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-17
 */
public class InterceptorTrafficStat implements Interceptor {
    private static final String TAG = InterceptorTrafficStat.class.getSimpleName();
    int mTrafficTag;

    public InterceptorTrafficStat(int trafficTag) {
        mTrafficTag = trafficTag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (mTrafficTag > 0) {
            TrafficStats.setThreadStatsTag(mTrafficTag);
        } else {
            Log.w(TAG, "invalid traffic tag " + mTrafficTag);
        }
        return chain.proceed(chain.request());
    }
}
