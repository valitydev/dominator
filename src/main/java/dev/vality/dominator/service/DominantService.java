package dev.vality.dominator.service;

import dev.vality.dominator.dao.dominant.iface.DominantDao;
import dev.vality.dominator.handler.dominant.DominantHandler;
import dev.vality.dominator.util.JsonUtil;
import dev.vality.damsel.domain_config.Commit;
import dev.vality.damsel.domain_config.Operation;
import dev.vality.geck.common.util.TypeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DominantService {

    private final DominantDao dominantDao;

    private final List<DominantHandler> handlers;

    @Transactional(propagation = Propagation.REQUIRED)
    public void processCommit(long versionId, Map.Entry<Long, Commit> entry) {
        Commit commit = entry.getValue();
        LocalDateTime createdAt = commit.getCreatedAt() == null
                ? null : TypeUtil.stringToLocalDateTime(commit.getCreatedAt());
        commit.getOps().forEach(operation -> handlers.forEach(handler -> {
            if (handler.acceptAndSet(operation)) {
                processOperation(handler, operation, createdAt, versionId);
            }
        }));
    }

    private void processOperation(DominantHandler handler,
                                  Operation operation,
                                  LocalDateTime createdAt,
                                  Long versionId) {
        try {
            log.debug("Start to process commit with versionId={} operation={} ",
                    versionId, JsonUtil.thriftBaseToJsonString(operation));
            handler.handle(operation, createdAt, versionId);
            log.debug("End to process commit with versionId={}", versionId);
        } catch (Exception ex) {
            log.error("The error was received when the service processed operation", ex);
            throw ex;
        }
    }

    public Optional<Long> getLastVersionId() {
        Optional<Long> lastVersionId = Optional.ofNullable(dominantDao.getLastVersionId());
        log.debug("Last dominant versionId={}", lastVersionId);
        return lastVersionId;
    }

    public void updateLastVersionId(Long lastVersionId) {
        dominantDao.updateLastVersionId(lastVersionId);
        log.trace("Last dominant versionId={} is updated", lastVersionId);
    }
}
