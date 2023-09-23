package io.github.hello09x.bedrock.database;


import io.github.hello09x.bedrock.database.typehandler.DefaultTypeHandler;
import io.github.hello09x.bedrock.database.typehandler.TypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableId {

    /**
     * @return 列名
     */
    String value();

    Class<? extends TypeHandler<?>> typeHandler() default DefaultTypeHandler.class;


}
