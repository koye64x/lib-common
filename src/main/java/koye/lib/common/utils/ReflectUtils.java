package koye.lib.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    public static List<Field> getDeclaredInheritedFields(Class<?> objectClass) {
        if (Object.class.equals(objectClass)) {
            return null;
        } else {
            List<Field> list = null;
            if (objectClass.getSuperclass() != null) {
                list = getDeclaredInheritedFields(objectClass.getSuperclass());
            }
            if (list == null) {
                list = new ArrayList<>();
            }

            for (Field f: objectClass.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    list.add(f);
                }
            }
            return list;
        }
    }

    public static boolean isPrimitiveType(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        if (Number.class.isAssignableFrom(type)
                || Boolean.class.equals(type)
                || String.class.equals(type)
                || Character.class.equals(type)
        ) {
            return true;
        }
        return false;
    }

    public static Class<?> getFieldGenericType(Field f) {
        ParameterizedType pt = (ParameterizedType) f.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    public static Field findField(Class<?> type, String fieldName) {
        if (type == null || type == Object.class) {
            return null;
        }
        for (Field declaredField : type.getDeclaredFields()) {
            if (declaredField.getName().equals(fieldName)) {
                return declaredField;
            }
        }
        return findField(type.getSuperclass(), fieldName);
    }

}
