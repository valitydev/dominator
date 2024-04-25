package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.constant.RevisionQuery;
import com.empayre.dominator.dao.party.iface.RevisionDao;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.impl.AbstractGenericDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class RevisionDaoImpl extends AbstractGenericDao implements RevisionDao {

    public RevisionDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveShopsRevision(String partyId, long revision) throws DaoException {
        getNamedParameterJdbcTemplate().update(RevisionQuery.SAVE_SHOPS_QUERY,
                new MapSqlParameterSource()
                        .addValue("party_id", partyId)
                        .addValue("revision", revision));
    }

    @Override
    public void saveContractsRevision(String partyId, long revision) throws DaoException {
        getNamedParameterJdbcTemplate().update(RevisionQuery.SAVE_CONTRACTS_QUERY,
                new MapSqlParameterSource()
                        .addValue("party_id", partyId)
                        .addValue("revision", revision));
    }

    @Override
    public void saveContractorsRevision(String partyId, long revision) throws DaoException {
        getNamedParameterJdbcTemplate().update(RevisionQuery.SAVE_CONTRACTORS_QUERY,
                new MapSqlParameterSource()
                        .addValue("party_id", partyId)
                        .addValue("revision", revision));
    }
}
