package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

import java.util.List;

public interface ContractAdjustmentDao extends GenericDao {
    void save(List<ContractAdjustment> contractAdjustmentList) throws DaoException;

    List<ContractAdjustment> getByContractId(Long contractId) throws DaoException;
}
