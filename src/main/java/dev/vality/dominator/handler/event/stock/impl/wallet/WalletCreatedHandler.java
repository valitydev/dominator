package dev.vality.dominator.handler.event.stock.impl.wallet;

import dev.vality.dominator.dao.wallet.iface.WalletDao;
import dev.vality.dominator.domain.tables.pojos.Wallet;
import dev.vality.dominator.factory.machine.event.MachineEventCopyFactory;
import dev.vality.fistful.wallet.Change;
import dev.vality.fistful.wallet.TimestampedChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletCreatedHandler implements WalletHandler {

    private final WalletDao walletDao;
    private final MachineEventCopyFactory<Wallet, String> walletMachineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("change.created", new IsNullCondition().not()));

    @Override
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        long sequenceId = event.getEventId();
        String walletId = event.getSourceId();
        log.info("Start wallet created handling, sequenceId={}, walletId={}", sequenceId, walletId);

        Wallet wallet =
                walletMachineEventCopyFactory.create(event, sequenceId, walletId, timestampedChange.getOccuredAt());

        wallet.setWalletName(change.getCreated().getName());
        wallet.setExternalId(change.getCreated().getExternalId());

        walletDao.save(wallet).ifPresentOrElse(
                id -> log.info("Wallet created has been saved, sequenceId={}, walletId={}", sequenceId, walletId),
                () -> log.info("Wallet created bound duplicated, sequenceId={}, walletId={}", sequenceId, walletId));
    }
}
