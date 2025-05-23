package dev.vality.dominator.listener;

import dev.vality.dominator.service.PartyManagementService;
import dev.vality.kafka.common.util.LogUtil;
import dev.vality.machinegun.eventsink.SinkEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartyManagementListener {

    private final PartyManagementService partyManagementService;

    @KafkaListener(
            autoStartup = "${kafka.topics.party-management.enabled}",
            topics = "${kafka.topics.party-management.id}",
            containerFactory = "partyManagementContainerFactory")
    public void handle(List<ConsumerRecord<String, SinkEvent>> messages, Acknowledgment ack) {
        log.info("Got partyManagement machineEvent batch with size: {}", messages.size());
        partyManagementService.handleEvents(
                messages.stream()
                        .map(m -> m.value().getEvent())
                        .collect(Collectors.toList())
        );
        ack.acknowledge();
        log.info("Batch partyManagement has been committed, size={}, {}", messages.size(),
                LogUtil.toSummaryStringWithSinkEventValues(messages));
    }
}
