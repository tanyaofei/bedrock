package io.github.hello09x.bedrock.storage.value;

import io.github.hello09x.bedrock.storage.JSONPersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class TagContainerArrayValueWrapper extends AbstractValueWrapper<JSONPersistentDataContainer[]> {

    public TagContainerArrayValueWrapper(@Nullable JSONPersistentDataContainer[] value) {
        super(value);
    }

}
