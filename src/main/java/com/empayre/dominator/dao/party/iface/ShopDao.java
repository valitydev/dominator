package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;
import com.empayre.dominator.domain.tables.pojos.Shop;

import java.util.Optional;

public interface ShopDao extends GenericDao {

    Optional<Long> save(Shop shop) throws DaoException;

    Shop get(String partyId, String shopId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

    void saveWithUpdateCurrent(Shop shopSource, Long oldEventId, String eventName);
}
