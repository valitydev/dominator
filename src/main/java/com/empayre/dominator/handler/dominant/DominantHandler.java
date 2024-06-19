package com.empayre.dominator.handler.dominant;

import java.time.LocalDateTime;

public interface DominantHandler<T> {

    boolean acceptAndSet(T change);

    void handle(T change, LocalDateTime createdAt, Long versionId);
}
