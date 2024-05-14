package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.ContractDao;
import com.empayre.dominator.domain.tables.pojos.Contract;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static com.empayre.dominator.domain.Tables.CONTRACT;

@Component
public class ContractDaoImpl extends AbstractDao implements ContractDao {

    public ContractDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Contract contract) throws DaoException {
        Long id = getDslContext()
                .insertInto(CONTRACT)
                .set(getDslContext().newRecord(CONTRACT, contract))
                .onConflict(CONTRACT.PARTY_ID, CONTRACT.CONTRACT_ID, CONTRACT.SEQUENCE_ID, CONTRACT.CHANGE_ID,
                        CONTRACT.CLAIM_EFFECT_ID)
                .doUpdate()
                .set(getDslContext().newRecord(CONTRACT, contract))
                .returning(CONTRACT.ID)
                .fetchOne()
                .getId();
        return Optional.ofNullable(id);
    }

    @NotNull
    @Override
    public Contract get(String partyId, String contractId) throws DaoException {
        Contract contract = getDslContext()
                .selectFrom(CONTRACT)
                .where(CONTRACT.PARTY_ID.eq(partyId)
                        .and(CONTRACT.CONTRACT_ID.eq(contractId))
                        .and(CONTRACT.CURRENT))
                .fetchOne()
                .into(Contract.class);

        return Optional.ofNullable(contract)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Contract not found, contractId='%s'", contractId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        getDslContext()
                .update(CONTRACT)
                .set(CONTRACT.CURRENT, false)
                .where(CONTRACT.ID.eq(id))
                .execute();
    }
}
