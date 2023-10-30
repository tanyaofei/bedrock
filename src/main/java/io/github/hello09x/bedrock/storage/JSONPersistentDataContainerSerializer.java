package io.github.hello09x.bedrock.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hello09x.bedrock.gson.typehandler.RuntimeTypeAdapterFactory;
import io.github.hello09x.bedrock.storage.value.*;
import lombok.Getter;

public class JSONPersistentDataContainerSerializer {

    @Getter
    private final Gson gson;

    public JSONPersistentDataContainerSerializer() {
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


}
