package koye.lib.expression;

import koye.lib.common.utils.ReflectUtils;

import java.lang.reflect.Field;

public class ValueGetterObject<T> extends ValueGetterAbstract<Object, T> {

    @Override
    public Object getValue(Object container, String name) {
        Field field = ReflectUtils.findField(container.getClass(), name);
        if (field == null) {
            throw new IllegalArgumentException("Field '"+name+"' not found in class " + container.getClass().getName());
        }
        field.setAccessible(true);
        try {
            return field.get(container);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
