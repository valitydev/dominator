package com.empayre.dominator.handler.event.stock.impl.partymngmnt;

import com.empayre.dominator.handler.event.stock.Handler;
import dev.vality.damsel.payment_processing.PartyChange;
import dev.vality.machinegun.eventsink.MachineEvent;

public interface PartyManagementHandler extends Handler<PartyChange, MachineEvent> {
}
