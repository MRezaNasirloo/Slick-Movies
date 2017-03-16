package com.github.pedramrn.slick.parent.library.middleware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public class RequestData {

    private Map<String, Object> map = new HashMap<>();

    public RequestData putInteger(String key, Integer i) {
        map.put(key, i);
        return this;
    }

    public RequestData putString(String key, String s) {
        map.put(key, s);
        return this;
    }

    public RequestData putObject(String key, Object o) {
        map.put(key, o);
        return this;
    }

    public Integer getInteger(String key, Integer def) {
        Object value;
        if ((value = map.get(key)) != null) {
            return ((Integer) value);
        }
        return def;
    }

    public String getSting(String key, String def) {
        Object value;
        if ((value = map.get(key)) != null) {
            return ((String) value);
        }
        return def;
    }

    public <R> R getObject(String key, R def, Class<R> type) throws ClassCastException {
        Object value;
        if ((value = map.get(key)) != null) {
            return type.cast(value);
        }
        return def;
    }

    public <R> R getParameter(R def, Class<R> type) throws ClassCastException {
        Object value;
        if ((value = map.get("$parameter$")) != null) {
            return type.cast(value);
        }
        return def;
    }

    public RequestData putParameter(Object o){
        map.put("$parameter$", o);
        return this;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }
}
