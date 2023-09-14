package com.salmalteam.salmal.support;

import java.lang.reflect.Field;

public class EntityIdManipulator {
    public static void setId(Object entity, Long id) {
        try {
            Class<?> clazz = entity.getClass();
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set ID using reflection", e);
        }
    }
}
