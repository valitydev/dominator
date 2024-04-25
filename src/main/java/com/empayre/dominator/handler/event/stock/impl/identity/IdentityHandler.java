package com.empayre.dominator.handler.event.stock.impl.identity;

import com.empayre.dominator.handler.event.stock.Handler;
import dev.vality.fistful.identity.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;

public interface IdentityHandler extends Handler<TimestampedChange, MachineEvent> {
}
