package com.haitao.framework.codec;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ParameterConvertor<T> implements IConverter<T, String[]> {

    public String[] properties = null;

    public ParameterConvertor() {
    }

    public ParameterConvertor(String[] properties) {
        this.properties = properties;
    }

    @Override
    public String[] converter(T entity) {
        if (null == entity) {
            return null;
        }
        String[] properties = getProperties(entity);

        if (null == properties) {
            return null;
        }
        String[] result = new String[2 * properties.length];
        for (int index = 0; index < properties.length; index++) {
            String property = properties[index];
            try {
                String data = (String) entity.getClass().getField(property).get(entity);
                result[2 * index] = property;
                if (null != data) {
                    result[2 * index + 1] = data;
                } else {
                    result[2 * index + 1] = "";
                }
            } catch (Exception e) {
                Log.e("CreateParam", e.toString());
            }
        }

        return result;
    }

    private String[] getProperties(T entity) {
        if (null == entity) {
            return null;
        }
        if (null == properties) {
            List<String> result = new ArrayList<String>();
            properties = new String[result.size()];
            properties = result.toArray(properties);

        }
        return properties;
    }
}
