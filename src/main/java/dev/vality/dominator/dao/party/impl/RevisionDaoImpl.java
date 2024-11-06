package dev.vality.dominator.dao.party.impl;

import dev.vality.dominator.constant.RevisionQuery;
import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.party.iface.RevisionDao;
import dev.vality.dominator.exception.DaoException;
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
