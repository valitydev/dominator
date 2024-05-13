package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.constant.RevisionQuery;
import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.RevisionDao;
import com.empayre.dominator.exception.DaoException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static org.jooq.impl.DSL.val;

@Component
public class RevisionDaoImpl extends AbstractDao implements RevisionDao {

    public RevisionDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveShopsRevision(String partyId, long revision) throws DaoException {
        getDslContext()
                .execute(RevisionQuery.SAVE_SHOPS_QUERY, val(revision), val(partyId));
    }

    @Override
    public void saveContractsRevision(String partyId, long revision) throws DaoException {
        getDslContext()
                .execute(RevisionQuery.SAVE_CONTRACTS_QUERY, val(revision), val(partyId));
    }

    @Override
    public void saveContractorsRevision(String partyId, long revision) throws DaoException {
        getDslContext()
                .execute(RevisionQuery.SAVE_CONTRACTORS_QUERY, val(revision), val(partyId));
    }
}
