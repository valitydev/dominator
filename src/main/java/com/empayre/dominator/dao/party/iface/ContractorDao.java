package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.domain.tables.pojos.Contractor;

import java.util.Optional;

public interface ContractorDao {

    Optional<Long> save(Contractor contractor) throws DaoException;

    Contractor get(String partyId, String contractorId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;
}
