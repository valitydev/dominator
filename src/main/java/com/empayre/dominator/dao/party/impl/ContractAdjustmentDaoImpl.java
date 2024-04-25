package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.party.iface.ContractAdjustmentDao;
import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.empayre.dominator.domain.Tables.CONTRACT_ADJUSTMENT;

@Component
public class ContractAdjustmentDaoImpl extends AbstractGenericDao implements ContractAdjustmentDao {

    private final RowMapper<ContractAdjustment> contractAdjustmentRowMapper;

    public ContractAdjustmentDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.contractAdjustmentRowMapper = new RecordRowMapper<>(CONTRACT_ADJUSTMENT, ContractAdjustment.class);
    }

    @Override
    public void save(List<ContractAdjustment> contractAdjustmentList) throws DaoException {
        List<Query> queries = contractAdjustmentList.stream()
                .map(contractAdjustment -> getDslContext().newRecord(CONTRACT_ADJUSTMENT, contractAdjustment))
                .map(contractAdjustmentRecord ->
                        getDslContext().insertInto(CONTRACT_ADJUSTMENT).set(contractAdjustmentRecord))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public List<ContractAdjustment> getByContractId(Long contractId) throws DaoException {
        Query query = getDslContext()
                .selectFrom(CONTRACT_ADJUSTMENT)
                .where(CONTRACT_ADJUSTMENT.CONTRACT_ID.eq(contractId))
                .orderBy(CONTRACT_ADJUSTMENT.ID.asc());
        return fetch(query, contractAdjustmentRowMapper);
    }
}
