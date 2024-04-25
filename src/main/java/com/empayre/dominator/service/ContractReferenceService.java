package com.empayre.dominator.service;

import com.empayre.dominator.dao.party.iface.ContractAdjustmentDao;
import com.empayre.dominator.dao.party.iface.PayoutToolDao;
import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.domain.tables.pojos.PayoutTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractReferenceService {

    private final ContractAdjustmentDao contractAdjustmentDao;
    private final PayoutToolDao payoutToolDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateContractReference(Long contractSourceId, Long contractId) {
        updateAdjustments(contractSourceId, contractId);
        updatePayoutTools(contractSourceId, contractId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePayoutTools(Long contractSourceId, Long contractId) {
        List<PayoutTool> payoutTools = payoutToolDao.getByContractId(contractSourceId);
        payoutTools.forEach(payoutTool -> {
            payoutTool.setId(null);
            payoutTool.setContractId(contractId);
        });
        payoutToolDao.save(payoutTools);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdjustments(Long contractSourceId, Long contractId) {
        List<ContractAdjustment> adjustments = contractAdjustmentDao.getByContractId(contractSourceId);
        adjustments.forEach(adjustment -> {
            adjustment.setId(null);
            adjustment.setContractId(contractId);
        });
        contractAdjustmentDao.save(adjustments);
    }
}
