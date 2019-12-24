package koye.lib.common.meta;

import koye.lib.common.utils.ReflectUtils;

import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FetchSchema extends TreeMap<String, FetchSchema> {

    public FetchSchema(String... propNames) {
        putProps(propNames);
    }

    public FetchSchema putProps(String... propNames) {
        for (String propName : propNames) {
            put(propName, null);
        }
        return this;
    }

    public FetchSchema putProps(Collection<String> propNames) {
        for (String propName : propNames) {
            put(propName, null);
        }
        return this;
    }

    public FetchSchema putProp(String propName, FetchSchema fetchSchema) {
        put(propName, fetchSchema);
        return this;
    }

    public static FetchSchema manyToOneOnly(Class<?> entityClass) {
        FetchSchema res = new FetchSchema();
        List<Field> fields = ReflectUtils.getFieldsByPropAnnotation(entityClass, ManyToOne.class);
        res.putProps(fields.stream().map(Field::getName).collect(Collectors.toList()));
        return res;
    }

}
