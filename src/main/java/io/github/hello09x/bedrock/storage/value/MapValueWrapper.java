package io.github.hello09x.bedrock.storage.value;

import io.github.hello09x.bedrock.storage.type.StringObjectMap;
import org.jetbrains.annotations.Nullable;

public class MapValueWrapper extends AbstractValueWrapper<StringObjectMap> {

    public MapValueWrapper(@Nullable StringObjectMap value) {
        super(value);
    }

}
