package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.domain.tables.pojos.PayoutTool;
import dev.vality.dominator.exception.DaoException;

import java.util.List;

public interface PayoutToolDao {
    void save(List<PayoutTool> payoutToolList) throws DaoException;

    List<PayoutTool> getByContractId(Long cntrctId) throws DaoException;
}
