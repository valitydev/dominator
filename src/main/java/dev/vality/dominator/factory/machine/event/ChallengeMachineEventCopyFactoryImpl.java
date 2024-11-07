package dev.vality.dominator.factory.machine.event;

import dev.vality.dominator.domain.tables.pojos.Challenge;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import org.springframework.stereotype.Component;

@Component
public class ChallengeMachineEventCopyFactoryImpl implements MachineEventCopyFactory<Challenge, String> {

    @Override
    public Challenge create(MachineEvent event, Long sequenceId, String identityId, Challenge old, String occurredAt) {
        Challenge challenge = old == null ? new Challenge() : new Challenge(old);
        challenge.setId(null);
        challenge.setWtime(null);
        challenge.setSequenceId(sequenceId.intValue());
        challenge.setIdentityId(identityId);
        challenge.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        challenge.setEventOccuredAt(TypeUtil.stringToLocalDateTime(occurredAt));
        return challenge;
    }

    @Override
    public Challenge create(MachineEvent event, Long sequenceId, String id, String occurredAt) {
        return create(event, sequenceId, id, null, occurredAt);
    }
}
