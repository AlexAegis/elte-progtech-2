package com.alexaegis.progtech.logic;

import java.util.Properties;

public class SecureProperties extends Properties {

    protected SecureProperties defaults;

    public synchronized Object setProperty(String key, char[] value) {
        return put(key, value);
    }

    public Object getPropertyObject(String key) {
        Object oval = super.get(key);
        return ((oval == null) && (defaults != null)) ? defaults.getProperty(key) : oval;
    }

    public synchronized Object setPropertyObject(String key, Object value) {
        return put(key, value);
    }

    public void destroyPasswordProperty(String key) {
        try {
            char[] chars = (char[]) getPropertyObject(key);
            for (int i = 0; i < chars.length; i++) {
                chars[i] = '0';
            }
            setPropertyObject(key, new Object());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}