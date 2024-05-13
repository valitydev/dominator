package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.PayoutToolDao;
import com.empayre.dominator.domain.tables.pojos.PayoutTool;
import com.empayre.dominator.exception.DaoException;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.empayre.dominator.domain.Tables.PAYOUT_TOOL;

@Component
public class PayoutToolDaoImpl extends AbstractDao implements PayoutToolDao {

    public PayoutToolDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(List<PayoutTool> payoutToolList) throws DaoException {
        List<Query> queries = payoutToolList.stream()
                .map(payoutTool -> getDslContext().newRecord(PAYOUT_TOOL, payoutTool))
                .map(payoutToolRecord -> getDslContext().insertInto(PAYOUT_TOOL).set(payoutToolRecord))
                .collect(Collectors.toList());
        getDslContext().batch(queries).execute();
    }

    @Override
    public List<PayoutTool> getByContractId(Long contractId) throws DaoException {
        return getDslContext()
                .selectFrom(PAYOUT_TOOL)
                .where(PAYOUT_TOOL.CONTRACT_ID.eq(contractId))
                .fetchInto(PayoutTool.class);
    }
}
