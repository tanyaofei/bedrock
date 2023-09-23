package io.github.hello09x.bedrock.task;

@FunctionalInterface
public interface Execution<T> {

    T execute() throws Throwable;

}
