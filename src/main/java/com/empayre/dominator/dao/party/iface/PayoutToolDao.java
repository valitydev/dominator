package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.domain.tables.pojos.PayoutTool;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

import java.util.List;

public interface PayoutToolDao extends GenericDao {
    void save(List<PayoutTool> payoutToolList) throws DaoException;

    List<PayoutTool> getByContractId(Long cntrctId) throws DaoException;
}
