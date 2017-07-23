package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
public class UserTraktTest {

    private Gson gson;
    private String json;

    @Before
    public void setUp() throws Exception {
        ModuleNetworkBase moduleNetworkBase = new ModuleNetworkBase();
        gson = moduleNetworkBase.baseGsonConverterFactory();
        json = "{\n" +
                "            \"username\": \"ctt2canuckt\",\n" +
                "            \"private\": false,\n" +
                "            \"name\": \"\",\n" +
                "            \"vip\": false,\n" +
                "            \"vip_ep\": false,\n" +
                "            \"ids\": {\n" +
                "                \"slug\": \"ctt2canuckt\"\n" +
                "            }\n" +
                "        }";
    }

    @Test
    public void id() throws Exception {
        UserTrakt userTrakt = gson.fromJson(json, UserTrakt.class);
        assertEquals(userTrakt.id(), "ctt2canuckt");
        assertEquals(userTrakt.username(), "ctt2canuckt");
    }

}