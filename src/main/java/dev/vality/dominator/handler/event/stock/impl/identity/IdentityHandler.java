package dev.vality.dominator.handler.event.stock.impl.identity;

import dev.vality.dominator.handler.event.stock.Handler;
import dev.vality.fistful.identity.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;

public interface IdentityHandler extends Handler<TimestampedChange, MachineEvent> {
}
