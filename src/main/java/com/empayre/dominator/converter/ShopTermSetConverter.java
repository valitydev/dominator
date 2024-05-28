package com.empayre.dominator.converter;

import com.empayre.dominator.dao.party.iface.ContractAdjustmentDao;
import com.empayre.dominator.dao.party.iface.TermSetHierarchyDao;
import com.empayre.dominator.data.ShopTermSetDataObject;
import com.empayre.dominator.domain.tables.pojos.ContractAdjustment;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.dominator.ShopTermSet;
import dev.vality.dominator.TermSetHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.empayre.dominator.util.TermSetConverterUtils.createEmptyTermSetHierarchyObject;
import static com.empayre.dominator.util.TermSetConverterUtils.replaceNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopTermSetConverter implements Converter<ShopTermSetDataObject, ShopTermSet> {

    private final TDeserializer deserializer;
    private final TermSetHierarchyDao termSetHierarchyDao;
    private final ContractAdjustmentDao contractAdjustmentDao;

    @Override
    public ShopTermSet convert(ShopTermSetDataObject source) {
        Long contractRecordId = source.getContractRecordId();
        ContractAdjustment lastAdjustment = contractAdjustmentDao.getLastByContractId(contractRecordId);
        Integer currentTermSetId = lastAdjustment == null ? source.getTermSetId() : lastAdjustment.getTermsId();
        TermSetHierarchy currentTermSet = termSetHierarchyDao.getCurrentTermSet(currentTermSetId);
        return new ShopTermSet()
                .setOwnerId(replaceNull(source.getPartyId()))
                .setShopId(replaceNull(source.getShopId()))
                .setContractId(replaceNull(source.getContractId()))
                .setShopName(replaceNull(source.getShopName()))
                .setTermSetName(replaceNull(currentTermSet.getName()))
                .setCurrency(replaceNull(source.getCurrency()))
                .setCurrentTermSet(deserializeTermSet(currentTermSet.getTermSetHierarchyObject()))
                .setTermSetHistory(getTermSetHistory(contractRecordId));
    }

    private List<TermSetHistory> getTermSetHistory(long contractRecordId) {
        return contractAdjustmentDao.getByContractId(contractRecordId).stream()
                .map(
                        adj -> new TermSetHistory()
                                .setTermSet(deserializeTermSet(
                                        termSetHierarchyDao.getTermSetHierarchyObject(adj.getTermsId(), true)))
                                .setAppliedAt(adj.getCreatedAt().toString())
                )
                .toList();
    }

    private TermSetHierarchyObject deserializeTermSet(byte[] object) {
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
