package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.domain.tables.pojos.ContractAdjustment;
import dev.vality.dominator.exception.DaoException;

import java.util.List;

public interface ContractAdjustmentDao {

    void save(List<ContractAdjustment> contractAdjustmentList) throws DaoException;

    List<ContractAdjustment> getByContractId(Long contractId) throws DaoException;

    ContractAdjustment getLastByContractId(Long contractId) throws DaoException;
}
