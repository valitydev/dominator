package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.exception.DaoException;
import dev.vality.dominator.domain.tables.pojos.Contractor;

import java.util.Optional;

public interface ContractorDao {

    Optional<Long> save(Contractor contractor) throws DaoException;

    Contractor get(String partyId, String contractorId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;
}
