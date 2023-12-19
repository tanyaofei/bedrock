package io.github.hello09x.bedrock.util.id;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IDWorker {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static long id() {
        return Snowflake.getId();
    }

    public static String stringId() {
        return String.valueOf(Snowflake.getId());
    }

}
