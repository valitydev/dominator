package com.empayre.dominator.handler.dominant;

import com.empayre.dominator.service.DominantService;
import dev.vality.damsel.domain_config.Commit;
import dev.vality.damsel.domain_config.RepositorySrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@DependsOn("flywayInitializer")
@RequiredArgsConstructor
public class DominantPoller {

    private final RepositorySrv.Iface dominantClient;
    private final DominantService dominantService;
    private final int maxQuerySize;
    private final boolean pollingEnabled;

    @Scheduled(fixedDelayString = "${dmt.polling.delay}")
    @SchedulerLock(name = "TaskScheduler_dominant_polling_process")
    public void process() {
        if (pollingEnabled) {
            final AtomicLong lastVersionId = new AtomicLong(dominantService.getLastVersionId().orElse(0L));
            try {
                Map<Long, Commit> pullRange = dominantClient.pullRange(lastVersionId.get(), maxQuerySize);
                pullRange.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> handleDominantData(lastVersionId, entry));
            } catch (Exception ex) {
                log.warn("Dominant polling error (lastVersionId={})", lastVersionId, ex);
            }
        }
    }

    private void handleDominantData(AtomicLong lastVersionId, Map.Entry<Long, Commit> entry) {
        AtomicLong versionId = new AtomicLong(entry.getKey());
        try {
            dominantService.processCommit(versionId.get(), entry);
            dominantService.updateLastVersionId(versionId.get());
            lastVersionId.set(versionId.get());
        } catch (RuntimeException ex) {
            throw new RuntimeException(
                    String.format("Unexpected error when polling dominant, versionId=%d", versionId.get()), ex);
        }
    }
}
