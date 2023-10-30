package io.github.hello09x.bedrock.storage.value;

import org.jetbrains.annotations.Nullable;

public class ByteArrayValueWrapper extends AbstractValueWrapper<byte[]> {

    public ByteArrayValueWrapper(byte @Nullable [] value) {
        super(value);
    }

}
