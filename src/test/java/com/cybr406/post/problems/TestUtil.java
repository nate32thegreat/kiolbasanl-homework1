package com.cybr406.post.problems;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestUtil {

    public static void testFieldGettersAndSetters(Class clazz, Class type, String fieldName, String getter, String setter) {
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        Field field = fields.stream()
                .filter(f -> f.getName().equals(fieldName))
                .findFirst()
                .orElse(null);

        assertNotNull(String.format("%s must have a %s property.", clazz.getName(), fieldName), field);
        assertEquals(String.format("The %s property must be type %s", fieldName, type.getName()), type, field.getType());
        assertFalse(String.format("The %s property should not be public", fieldName), Modifier.isPublic(field.getModifiers()));

        Method method = methods.stream()
                .filter(m -> m.getName().equals(getter))
                .findFirst()
                .orElse(null);

        assertNotNull(String.format("The %s object must have a %s method.", clazz.getName(), getter), method);
        assertEquals(String.format("The %s method must return a %s", getter, type), type, method.getReturnType());
        assertTrue(String.format("The %s method must be public.", getter), Modifier.isPublic(method.getModifiers()));

        method = methods.stream()
                .filter(m -> m.getName().equals(setter))
                .findFirst()
                .orElse(null);

        assertNotNull(String.format("The %s object must have a %s method.", clazz.getName(), setter), method);
        assertEquals(String.format("The %s method must be void.", setter), Void.TYPE, method.getReturnType());
        assertTrue(String.format("The %s method must be public.", setter), Modifier.isPublic(method.getModifiers()));
    }

}
