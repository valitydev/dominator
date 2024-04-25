package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.party.iface.ContractorDao;
import com.empayre.dominator.domain.tables.pojos.Contractor;
import com.empayre.dominator.domain.tables.records.ContractorRecord;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.empayre.dominator.domain.Tables.CONTRACTOR;


@Component
public class ContractorDaoImpl extends AbstractGenericDao implements ContractorDao {

    private final RowMapper<Contractor> contractorRowMapper;

    public ContractorDaoImpl(DataSource dataSource) {
        super(dataSource);
        contractorRowMapper = new RecordRowMapper<>(CONTRACTOR, Contractor.class);
    }

    @Override
    public Optional<Long> save(Contractor contractor) throws DaoException {
        ContractorRecord record = getDslContext().newRecord(CONTRACTOR, contractor);
        Query query = getDslContext()
                .insertInto(CONTRACTOR)
                .set(record)
                .onConflict(CONTRACTOR.PARTY_ID, CONTRACTOR.CONTRACTOR_ID, CONTRACTOR.SEQUENCE_ID, CONTRACTOR.CHANGE_ID,
                        CONTRACTOR.CLAIM_EFFECT_ID)
                .doNothing()
                .returning(CONTRACTOR.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Contractor get(String partyId, String contractorId) throws DaoException {
        Query query = getDslContext().selectFrom(CONTRACTOR)
                .where(CONTRACTOR.PARTY_ID.eq(partyId)
                        .and(CONTRACTOR.CONTRACTOR_ID.eq(contractorId))
                        .and(CONTRACTOR.CURRENT));
        return Optional.ofNullable(fetchOne(query, contractorRowMapper))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Contractor not found, contractorId='%s'", contractorId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext()
                .update(CONTRACTOR)
                .set(CONTRACTOR.CURRENT, false)
                .where(CONTRACTOR.ID.eq(id).and(CONTRACTOR.CURRENT));
        executeOne(query);
    }
}