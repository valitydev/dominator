package dev.vality.dominator.handler.event.stock.impl.identity;

import dev.vality.dominator.dao.identity.iface.IdentityDao;
import dev.vality.dominator.domain.tables.pojos.Identity;
import dev.vality.dominator.factory.machine.event.MachineEventCopyFactory;
import dev.vality.fistful.identity.Change;
import dev.vality.fistful.identity.TimestampedChange;
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
public class IdentityEffectiveChallengeChangedHandler implements IdentityHandler {

    private final IdentityDao identityDao;
    private final MachineEventCopyFactory<Identity, String> identityMachineEventCopyFactory;

    @Getter
    private Filter filter = new PathConditionFilter(
            new PathConditionRule("change.effective_challenge_changed", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        long sequenceId = event.getEventId();
        String identityId = event.getSourceId();
        log.info("Start effective identity challenge changed handling, sequenceId={}, identityId={}", sequenceId,
                identityId);
        final Identity identityOld = identityDao.get(identityId);
        Identity identityNew = identityMachineEventCopyFactory
                .create(event, sequenceId, identityId, identityOld, timestampedChange.getOccuredAt());

        identityNew.setIdentityEffectiveChalengeId(change.getEffectiveChallengeChanged());

        identityDao.save(identityNew).ifPresentOrElse(
                id -> {
                    identityDao.updateNotCurrent(identityOld.getId());
                    log.info("Effective identity challenge have been changed, sequenceId={}, identityId={}", sequenceId,
                            identityId);
                },
                () -> log.info("Effective identity challenge have been saved, sequenceId={}, identityId={}", sequenceId,
                        identityId));
    }

}
