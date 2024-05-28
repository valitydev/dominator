package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.ContractAdjustmentDao;
import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.exception.DaoException;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.empayre.dominator.domain.Tables.CONTRACT_ADJUSTMENT;

@Component
public class ContractAdjustmentDaoImpl extends AbstractDao implements ContractAdjustmentDao {

    public ContractAdjustmentDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(List<ContractAdjustment> contractAdjustmentList) throws DaoException {
        List<Query> queries = contractAdjustmentList.stream()
                .map(contractAdjustment -> getDslContext().newRecord(CONTRACT_ADJUSTMENT, contractAdjustment))
                .map(contractAdjustmentRecord ->
                        getDslContext()
                                .insertInto(CONTRACT_ADJUSTMENT)
                                .set(contractAdjustmentRecord))
                .collect(Collectors.toList());
        getDslContext()
                .batch(queries)
                .execute();
    }

    @Override
    public List<ContractAdjustment> getByContractId(Long contractId) throws DaoException {
        return getDslContext()
                .selectFrom(CONTRACT_ADJUSTMENT)
                .where(CONTRACT_ADJUSTMENT.CONTRACT_ID.eq(contractId))
                .orderBy(CONTRACT_ADJUSTMENT.ID.asc())
                .fetch()
                .into(ContractAdjustment.class);
    }

    @Override
    public ContractAdjustment getLastByContractId(Long contractId) throws DaoException {
        return getDslContext()
                .selectFrom(CONTRACT_ADJUSTMENT)
                .where(CONTRACT_ADJUSTMENT.CONTRACT_ID.eq(contractId))
                .orderBy(CONTRACT_ADJUSTMENT.ID.desc())
                .limit(1)
                .fetchOneInto(ContractAdjustment.class);
    }
}
