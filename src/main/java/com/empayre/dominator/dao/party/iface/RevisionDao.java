package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;

public interface RevisionDao {

    void saveShopsRevision(String partyId, long revision) throws DaoException;

    void saveContractsRevision(String partyId, long revision) throws DaoException;

    void saveContractorsRevision(String partyId, long revision) throws DaoException;
}
