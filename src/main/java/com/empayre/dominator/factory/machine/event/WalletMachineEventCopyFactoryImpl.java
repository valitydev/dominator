package com.empayre.dominator.factory.machine.event;

import com.empayre.dominator.domain.tables.pojos.Wallet;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import org.springframework.stereotype.Component;

@Component
public class WalletMachineEventCopyFactoryImpl implements MachineEventCopyFactory<Wallet, String> {

    @Override
    public Wallet create(MachineEvent event, Long sequenceId, String id, Wallet walletOld, String occurredAt) {
        Wallet wallet = walletOld == null ? new Wallet() : new Wallet(walletOld);
        wallet.setId(null);
        wallet.setWtime(null);
        wallet.setSequenceId(sequenceId.intValue());
        wallet.setWalletId(id);
        wallet.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        wallet.setEventOccuredAt(TypeUtil.stringToLocalDateTime(occurredAt));
        return wallet;
    }

    @Override
    public Wallet create(MachineEvent event, Long sequenceId, String id, String occurredAt) {
        return create(event, sequenceId, id, null, occurredAt);
    }
}
