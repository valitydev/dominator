package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.ShopDao;
import com.empayre.dominator.domain.tables.pojos.Shop;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static com.empayre.dominator.domain.Tables.SHOP;

@Slf4j
@Component
public class ShopDaoImpl extends AbstractDao implements ShopDao {

    public ShopDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Shop shop) throws DaoException {
        Long id = getDslContext()
                .insertInto(SHOP)
                .set(getDslContext().newRecord(SHOP, shop))
                .onConflict(SHOP.PARTY_ID, SHOP.SHOP_ID, SHOP.SEQUENCE_ID, SHOP.CHANGE_ID, SHOP.CLAIM_EFFECT_ID)
                .doUpdate()
                .set(getDslContext().newRecord(SHOP, shop))
                .returning(SHOP.ID)
                .fetchOne()
                .getId();
        return Optional.ofNullable(id);
    }

    @NotNull
    @Override
    public Shop get(String partyId, String shopId) throws DaoException {
        var result = getDslContext()
                .selectFrom(SHOP)
                .where(SHOP.PARTY_ID.eq(partyId)
                        .and(SHOP.SHOP_ID.eq(shopId))
                        .and(SHOP.CURRENT))
                .fetchOne()
                .into(Shop.class);
        return Optional.ofNullable(result)
                .orElseThrow(() -> new NotFoundException(String.format("Shop not found, shopId='%s'", shopId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        getDslContext()
                .update(SHOP)
                .set(SHOP.CURRENT, false)
                .where(SHOP.ID.eq(id))
                .execute();
    }

    @Override
    public void saveWithUpdateCurrent(Shop shopSource, Long oldEventId, String eventName) {
        save(shopSource).ifPresentOrElse(
                atLong -> {
                    updateNotCurrent(oldEventId);
                    log.info("Shop {} has been saved, sequenceId={}, partyId={}, shopId={}, changeId={}",
                            eventName, shopSource.getSequenceId(), shopSource.getPartyId(), shopSource.getShopId(),
                            shopSource.getChangeId());
                },
                () -> log.info("Shop {} duplicated, sequenceId={}, partyId={}, shopId={}, changeId={}",
                        eventName, shopSource.getSequenceId(), shopSource.getPartyId(), shopSource.getShopId(),
                        shopSource.getChangeId())
        );
    }
}
