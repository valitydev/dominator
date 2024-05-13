package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.domain.tables.pojos.Party;

import java.util.Optional;

public interface PartyDao {
    Optional<Long> save(Party party) throws DaoException;

    Party get(String partyId) throws DaoException;

    void updateNotCurrent(Long partyId) throws DaoException;

    void saveWithUpdateCurrent(Party partySource, Long oldId, String eventName);
}
