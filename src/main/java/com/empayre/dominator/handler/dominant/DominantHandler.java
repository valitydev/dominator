package com.empayre.dominator.handler.dominant;

public interface DominantHandler<T> {

    boolean acceptAndSet(T change);

    void handle(T change, Long versionId);
}
