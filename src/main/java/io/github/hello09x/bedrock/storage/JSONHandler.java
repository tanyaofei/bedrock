package io.github.hello09x.bedrock.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hello09x.bedrock.gson.typehandler.RuntimeTypeAdapterFactory;
import io.github.hello09x.bedrock.storage.value.*;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONHandler {

    @Getter
    private final Gson gson;

    public JSONHandler() {
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                        .of(AbstractValueWrapper.class, "type")

                        .registerSubtype(ByteValueWrapper.class, "byte")
                        .registerSubtype(ShortValueWrapper.class, "short")
                        .registerSubtype(IntValueWrapper.class, "int")
                        .registerSubtype(LongValueWrapper.class, "long")
                        .registerSubtype(FloatValueWrapper.class, "float")
                        .registerSubtype(DoubleValueWrapper.class, "double")
                        .registerSubtype(StringValueWrapper.class, "string")

                        .registerSubtype(IntArrayValueWrapper.class, "int[]")
                        .registerSubtype(LongArrayValueWrapper.class, "long[]")
                        .registerSubtype(ByteArrayValueWrapper.class, "byte[]")
                        .registerSubtype(StringArrayValueWrapper.class, "string[]")

                        .registerSubtype(TagContainerValueWrapper.class, "tag")
                        .registerSubtype(TagContainerArrayValueWrapper.class, "tag[]")
                ).create();
    }

    @Contract(pure = true, value = "null -> null")
    public String toJSON(@Nullable Object object) {
        return this.gson.toJson(object);
    }

    @Contract(pure = true, value = "null, _ -> null")
    public <T> @Nullable T fromJSON(@Nullable String json, Class<T> type) {
        return this.gson.fromJson(json, type);
    }

    public <T> @Nullable T fromFile(@NotNull File file, Class<T> type) throws IOException {
        try (var reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return this.gson.fromJson(reader, type);
        }
    }

}
