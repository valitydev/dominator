package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.domain.tables.pojos.Contract;
import dev.vality.dominator.exception.DaoException;

import java.util.Optional;

public interface ContractDao {

    Optional<Long> save(Contract contract) throws DaoException;

    Contract get(String partyId, String contractId) throws DaoException;

    void updateNotCurrent(Long contractId) throws DaoException;
}
