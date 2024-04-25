package com.empayre.dominator.handler.event.stock.impl.partymngmnt.party;

import com.empayre.dominator.dao.party.iface.PartyDao;
import com.empayre.dominator.handler.event.stock.impl.partymngmnt.PartyManagementHandler;
import dev.vality.damsel.domain.Blocking;
import dev.vality.damsel.payment_processing.PartyChange;
import com.empayre.dominator.domain.tables.pojos.Party;
import com.empayre.dominator.factory.machine.event.MachineEventCopyFactory;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartyBlockingHandler implements PartyManagementHandler {

    private final PartyDao partyDao;
    private final MachineEventCopyFactory<Party, Integer> partyIntegerMachineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("party_blocking", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(PartyChange change, MachineEvent event, Integer changeId) {
        long sequenceId = event.getEventId();
        Blocking partyBlocking = change.getPartyBlocking();
        String partyId = event.getSourceId();
        log.info("Start party blocking handling, sequenceId={}, partyId={}, changeId={}", sequenceId, partyId,
                changeId);
        Party partyOld = partyDao.get(partyId);
        Party partyNew = partyIntegerMachineEventCopyFactory.create(event, sequenceId, changeId, partyOld, null);

        partyNew.setBlocking(
                TBaseUtil.unionFieldToEnum(partyBlocking, com.empayre.dominator.domain.enums.Blocking.class));
        if (partyBlocking.isSetUnblocked()) {
            partyNew.setBlockingUnblockedReason(partyBlocking.getUnblocked().getReason());
            partyNew.setBlockingUnblockedSince(TypeUtil.stringToLocalDateTime(partyBlocking.getUnblocked().getSince()));
            partyNew.setBlockingBlockedReason(null);
            partyNew.setBlockingBlockedSince(null);
        } else if (partyBlocking.isSetBlocked()) {
            partyNew.setBlockingUnblockedReason(null);
            partyNew.setBlockingUnblockedSince(null);
            partyNew.setBlockingBlockedReason(partyBlocking.getBlocked().getReason());
            partyNew.setBlockingBlockedSince(TypeUtil.stringToLocalDateTime(partyBlocking.getBlocked().getSince()));
        }

        partyDao.saveWithUpdateCurrent(partyNew, partyOld.getId(), "blocking");
    }

}
