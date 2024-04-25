package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

public interface RevisionDao extends GenericDao {

    void saveShopsRevision(String partyId, long revision) throws DaoException;

    void saveContractsRevision(String partyId, long revision) throws DaoException;

    void saveContractorsRevision(String partyId, long revision) throws DaoException;
}
