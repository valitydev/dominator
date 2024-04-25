package com.empayre.dominator.factory.machine.event;

import com.empayre.dominator.domain.tables.pojos.Party;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import org.springframework.stereotype.Component;

@Component
public class PartyMachineEventCopyFactoryImpl implements MachineEventCopyFactory<Party, Integer> {

    @Override
    public Party create(MachineEvent event, Long sequenceId, Integer id, Party old, String occurredAt) {
        Party party = old == null ? new Party() : new Party(old);
        party.setId(null);
        party.setWtime(null);
        party.setSequenceId(sequenceId.intValue());
        party.setChangeId(id);
        party.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        return party;
    }

    @Override
    public Party create(MachineEvent event, Long sequenceId, Integer id, String occurredAt) {
        return create(event, sequenceId, id, null, occurredAt);
    }
}
