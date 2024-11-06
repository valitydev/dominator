package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.exception.DaoException;

public interface RevisionDao {

    void saveShopsRevision(String partyId, long revision) throws DaoException;

    void saveContractsRevision(String partyId, long revision) throws DaoException;

    void saveContractorsRevision(String partyId, long revision) throws DaoException;
}
