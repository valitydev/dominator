package com.empayre.dominator.handler.event.stock.impl.partymngmnt.shop;

import dev.vality.damsel.payment_processing.ClaimEffect;
import dev.vality.damsel.payment_processing.PartyChange;
import dev.vality.damsel.payment_processing.ScheduleChanged;
import dev.vality.damsel.payment_processing.ShopEffectUnit;
import com.empayre.dominator.dao.party.iface.ShopDao;
import com.empayre.dominator.domain.tables.pojos.Shop;
import com.empayre.dominator.factory.claim.effect.ClaimEffectCopyFactory;
import com.empayre.dominator.handler.event.stock.impl.partymngmnt.AbstractClaimChangedHandler;
import dev.vality.machinegun.eventsink.MachineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopPayoutScheduleChangedHandler extends AbstractClaimChangedHandler {

    private final ShopDao shopDao;
    private final ClaimEffectCopyFactory<Shop, Integer> claimEffectCopyFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(PartyChange change, MachineEvent event, Integer changeId) {
        long sequenceId = event.getEventId();
        List<ClaimEffect> claimEffects = getClaimStatus(change).getAccepted().getEffects();
        for (int i = 0; i < claimEffects.size(); i++) {
            ClaimEffect claimEffect = claimEffects.get(i);
            if (claimEffect.isSetShopEffect() && claimEffect.getShopEffect().getEffect().isSetPayoutScheduleChanged()) {
                handleEvent(event, changeId, sequenceId, claimEffects.get(i), i);
            }
        }
    }

    private void handleEvent(MachineEvent event, Integer changeId, long sequenceId, ClaimEffect e,
                             Integer claimEffectId) {
        ShopEffectUnit shopEffect = e.getShopEffect();
        ScheduleChanged payoutScheduleChanged = shopEffect.getEffect().getPayoutScheduleChanged();
        String shopId = shopEffect.getShopId();
        String partyId = event.getSourceId();
        log.info("Start shop payoutScheduleChanged handling, sequenceId={}, partyId={}, shopId={}, changeId={}",
                sequenceId, partyId, shopId, changeId);

        final Shop shopOld = shopDao.get(partyId, shopId);
        Shop shopNew = claimEffectCopyFactory.create(event, sequenceId, claimEffectId, changeId, shopOld);

        if (payoutScheduleChanged.isSetSchedule()) {
            shopNew.setPayoutScheduleId(payoutScheduleChanged.getSchedule().getId());
        } else {
            shopNew.setPayoutScheduleId(null);
        }

        shopDao.saveWithUpdateCurrent(shopNew, shopOld.getId(), "payoutScheduleChanged");
    }
}
