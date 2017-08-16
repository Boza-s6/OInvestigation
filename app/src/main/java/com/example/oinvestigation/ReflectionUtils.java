package com.example.oinvestigation;

import java.lang.reflect.Field;

/**
 * Created by nemanja on 24.5.17..
 */

public class ReflectionUtils {

    public static String getAllFields(Object o) {
        StringBuilder result = new StringBuilder("{");
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                result.append(field.getName()).append(": ").append(field.get(o));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            result.append("\n");
        }
        result.append("}");
        return result.toString();
    }
}
