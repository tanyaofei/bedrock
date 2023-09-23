package io.github.hello09x.bedrock.database;

import com.google.common.base.CaseFormat;
import io.github.hello09x.bedrock.database.typehandler.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @param fieldName     字段名称
 * @param columnName   列名
 * @param primary  是否主键
 * @param javaType 类型
 */
public record ColumnInfo(
        String fieldName,
        String columnName,
        boolean primary,
        Class<?> javaType,

        TypeHandler<?> typeHandler
) {
    public static List<ColumnInfo> parse(Class<?> type) {
        if (type.isRecord()) {
            return Arrays.stream(type.getRecordComponents()).map(ColumnInfo::parse).toList();
        } else {
            return Arrays.stream(type.getDeclaredFields()).map(ColumnInfo::parse).toList();
        }
    }

    public static @NotNull ColumnInfo parse(Field field) {
        return parse(
                field.getName(),
                field.getAnnotation(TableId.class),
                field.getAnnotation(TableField.class),
                field.getType()
        );
    }

    public static @NotNull ColumnInfo parse(RecordComponent rc) {
        return parse(
                rc.getName(),
                rc.getAnnotation(TableId.class),
                rc.getAnnotation(TableField.class),
                rc.getType()
        );
    }

    private static @NotNull ColumnInfo parse(
            @NotNull String name,
            @Nullable TableId tableId,
            @Nullable TableField tableField,
            @NotNull Class<?> javaType
    ) {
        String column;
        Class<? extends TypeHandler<?>> typeHandler;
        boolean primary;
        if (tableField != null) {
            column = tableField.value();
            typeHandler = tableField.typeHandler();
            primary = false;
        } else if (tableId != null) {
            column = tableId.value();
            typeHandler = tableId.typeHandler();
            primary = true;
        } else {
            column = null;
            typeHandler = null;
            primary = false;
        }

        if (column == null) {
            column = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        }
        if (typeHandler == null) {
            typeHandler = DefaultTypeHandler.class;
        }

        return new ColumnInfo(
                name,
                column,
                primary,
                javaType,
                initHandler(typeHandler, javaType)
        );
    }

    private static TypeHandler<?> initHandler(
            @NotNull Class<? extends TypeHandler<?>> typeHandler,
            @NotNull Class<?> javaType
    ) {
        if (typeHandler == DefaultTypeHandler.class) {
            return initTypeHandler(javaType);
        }

        if (typeHandler == JsonTypeHandler.class) {
            return new JsonTypeHandler((Class<Object>) javaType);
        }

        try {
            return typeHandler.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException e
        ) {
            throw new IllegalStateException("Failed to new instance of type handler: " + typeHandler);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static DefaultTypeHandler initTypeHandler(
            @NotNull Class<?> javaType
    ) {
        TypeHandler<?> delegate;
        if (javaType == boolean.class || javaType == Boolean.class) {
            delegate = new BooleanTypeHandler();
        } else if (javaType == byte.class || javaType == Byte.class) {
            delegate = new BytesTypeHandler();
        } else if (javaType == short.class || javaType == Short.class) {
            delegate = new ShortTypeHandler();
        } else if (javaType == int.class || javaType == Integer.class) {
            delegate = new IntegerTypeHandler();
        } else if (javaType == long.class || javaType == Long.class) {
            delegate = new LongTypeHandler();
        } else if (javaType == float.class || javaType == Float.class) {
            delegate = new FloatTypeHandler();
        } else if (javaType == double.class || javaType == Double.class) {
            delegate = new DoubleTypeHandler();
        } else if (javaType == BigDecimal.class) {
            delegate = new BigDecimalTypeHandler();
        } else if (javaType == String.class) {
            delegate = new StringTypeHandler();
        } else if (Enum.class.isAssignableFrom(javaType)) {
            delegate = new EnumTypeHandler<>((Class<? extends Enum>) javaType);
        } else if (javaType == LocalTime.class) {
            delegate = new LocalTimeTypeHandler();
        } else if (javaType == LocalDate.class) {
            delegate = new LocalDateTypeHandler();
        } else if (javaType == LocalDateTime.class) {
            delegate = new LocalDateTimeTypeHandler();
        } else if (javaType == Date.class) {
            delegate = new DateTypeHandler();
        } else if (javaType == byte[].class) {
            delegate = new BytesTypeHandler();
        } else if (javaType == UUID.class) {
            delegate = new UUIDTypeHandler();
        } else {
            throw new IllegalArgumentException("No type handler for: " + javaType);
        }
        return new DefaultTypeHandler((TypeHandler<Object>) delegate);
    }

}
