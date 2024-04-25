package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;
import com.empayre.dominator.domain.tables.pojos.Contractor;

import java.util.Optional;

public interface ContractorDao extends GenericDao {

    Optional<Long> save(Contractor contractor) throws DaoException;

    Contractor get(String partyId, String contractorId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;
}
