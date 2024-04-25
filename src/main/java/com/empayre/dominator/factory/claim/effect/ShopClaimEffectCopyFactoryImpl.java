package com.empayre.dominator.factory.claim.effect;

import com.empayre.dominator.domain.tables.pojos.Shop;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import org.springframework.stereotype.Component;

@Component
public class ShopClaimEffectCopyFactoryImpl implements ClaimEffectCopyFactory<Shop, Integer> {

    @Override
    public Shop create(MachineEvent event, long sequenceId, Integer claimEffectId, Integer id,
                       Shop old) {
        Shop shop = old == null ? new Shop() : new Shop(old);
        shop.setId(null);
        shop.setWtime(null);
        shop.setSequenceId((int) sequenceId);
        shop.setChangeId(id);
        shop.setClaimEffectId(claimEffectId);
        shop.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        return shop;
    }
}
