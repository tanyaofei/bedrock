package io.github.hello09x.bedrock.database;

import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public record TableInfo(

        @NotNull Class<?> type,

        @NotNull String tableName,

        @Nullable ColumnInfo primaryKey,

        @NotNull List<ColumnInfo> columns,

        @NotNull Constructor<?> constructor
) {

    private final static ConcurrentMap<Class<?>, TableInfo> cache = new ConcurrentHashMap<>();

    public static @NotNull TableInfo parse(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, k -> {
            var table = clazz.getAnnotation(Table.class);

            String name;
            if (table == null) {
                name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
            } else {
                name = table.value();
            }

            var columns = ColumnInfo.parse(clazz);

            return new TableInfo(
                    clazz,
                    name,
                    columns.stream().filter(ColumnInfo::primary).findAny().orElse(null),
                    columns,
                    findConstructor(clazz, columns)
            );
        });
    }

    private static Constructor<?> findConstructor(Class<?> type, List<ColumnInfo> columns) {
        if (type.isRecord()) {
            try {
                return type.getConstructor(columns.stream().map(ColumnInfo::javaType).toArray(Class[]::new));
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Missing construct for " + type);
            }
        } else {
            try {
                return type.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Missing no args construct for " + type);
            }
        }
    }

    public Object newInstance(Object[] parameters) {
        try {
            if (type.isRecord()) {
                return constructor.newInstance(parameters);
            } else {
                var obj = constructor.newInstance();
                var fields = type.getDeclaredFields();
                if (fields.length != parameters.length) {
                    throw new IllegalArgumentException(fields.length + " parameters expected but got " + parameters.length);
                }
                for (int i = 0; i < parameters.length; i++) {
                    var field = fields[i];
                    var param = parameters[i];
                    field.setAccessible(true);
                    field.set(obj, param);
                }
                return obj;
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to new instance for " + type, e);
        }
    }

}
