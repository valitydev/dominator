package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.exception.DaoException;
import dev.vality.dominator.domain.tables.pojos.Shop;

import java.util.Optional;

public interface ShopDao {

    Optional<Long> save(Shop shop) throws DaoException;

    Shop get(String partyId, String shopId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

    void saveWithUpdateCurrent(Shop shopSource, Long oldEventId, String eventName);
}
