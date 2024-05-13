package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.domain.tables.pojos.Contract;
import com.empayre.dominator.exception.DaoException;

import java.util.Optional;

public interface ContractDao {

    Optional<Long> save(Contract contract) throws DaoException;

    Contract get(String partyId, String contractId) throws DaoException;

    void updateNotCurrent(Long contractId) throws DaoException;
}
