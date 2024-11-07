package dev.vality.dominator.dao.party.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.party.iface.ContractorDao;
import dev.vality.dominator.domain.tables.pojos.Contractor;
import dev.vality.dominator.exception.DaoException;
import dev.vality.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.dominator.domain.Tables.CONTRACTOR;

@Component
public class ContractorDaoImpl extends AbstractDao implements ContractorDao {

    public ContractorDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Contractor contractor) throws DaoException {
        Long id = getDslContext()
                .insertInto(CONTRACTOR)
                .set(getDslContext().newRecord(CONTRACTOR, contractor))
                .onConflict(CONTRACTOR.PARTY_ID, CONTRACTOR.CONTRACTOR_ID, CONTRACTOR.SEQUENCE_ID, CONTRACTOR.CHANGE_ID,
                        CONTRACTOR.CLAIM_EFFECT_ID)
                .doNothing()
                .returning(CONTRACTOR.ID)
                .fetchOne()
                .getId();

        return Optional.ofNullable(id).map(Number::longValue);
    }

    @NotNull
    @Override
    public Contractor get(String partyId, String contractorId) throws DaoException {
        var contractor = getDslContext().selectFrom(CONTRACTOR)
                .where(CONTRACTOR.PARTY_ID.eq(partyId)
                        .and(CONTRACTOR.CONTRACTOR_ID.eq(contractorId))
                        .and(CONTRACTOR.CURRENT))
                .fetchOne()
                .into(Contractor.class);
        return Optional.ofNullable(contractor)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Contractor not found, contractorId='%s'", contractorId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        getDslContext()
                .update(CONTRACTOR)
                .set(CONTRACTOR.CURRENT, false)
                .where(CONTRACTOR.ID.eq(id).and(CONTRACTOR.CURRENT))
                .execute();
    }
}
