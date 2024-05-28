package com.empayre.dominator.service;

import com.empayre.dominator.dao.party.iface.ContractAdjustmentDao;
import com.empayre.dominator.dao.party.iface.TermSetHierarchyDao;
import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.dominator.TermSetHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.empayre.dominator.util.TermSetConverterUtils.createEmptyTermSetHierarchyObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermSetGettingService {

    private final TDeserializer deserializer;
    private final TermSetHierarchyDao termSetHierarchyDao;
    private final ContractAdjustmentDao contractAdjustmentDao;

    public TermSetHierarchy getCurrentTermSet(Long contractRecordId, Integer defaultTermSetId) {
        ContractAdjustment lastAdjustment = contractAdjustmentDao.getLastByContractId(contractRecordId);
        Integer currentTermSetId = lastAdjustment == null ? defaultTermSetId : lastAdjustment.getTermsId();
        return termSetHierarchyDao.getCurrentTermSet(currentTermSetId);
    }

    public List<TermSetHistory> getTermSetHistory(long contractRecordId) {
        return contractAdjustmentDao.getByContractId(contractRecordId).stream()
                .map(
                        adj -> new TermSetHistory()
                                .setTermSet(getTermSetFromObject(
                                        termSetHierarchyDao.getTermSetHierarchyObject(adj.getTermsId(), true)))
                                .setAppliedAt(adj.getCreatedAt().toString())
                )
                .toList();
    }

    public TermSetHierarchyObject getTermSetFromObject(byte[] object) {
        try {
            if (object == null || object.length == 0) {
                return createEmptyTermSetHierarchyObject();
            }
            TermSetHierarchyObject termSetHierarchyObject = new TermSetHierarchyObject();
            deserializer.deserialize(termSetHierarchyObject, object);
            return termSetHierarchyObject;
        } catch (TException e) {
            log.error("TermSetHierarchyObject deserialization exception", e);
            return createEmptyTermSetHierarchyObject();
        }
    }
}
