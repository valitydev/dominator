package com.empayre.dominator.handler.event.stock.impl.wallet;

import com.empayre.dominator.handler.event.stock.Handler;
import dev.vality.fistful.wallet.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;

public interface WalletHandler extends Handler<TimestampedChange, MachineEvent> {
}
