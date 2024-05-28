package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.exception.DaoException;

import java.util.List;

public interface ContractAdjustmentDao {

    void save(List<ContractAdjustment> contractAdjustmentList) throws DaoException;

    List<ContractAdjustment> getByContractId(Long contractId) throws DaoException;

    ContractAdjustment getLastByContractId(Long contractId) throws DaoException;
}
