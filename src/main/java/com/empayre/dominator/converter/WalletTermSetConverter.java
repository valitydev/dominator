package com.empayre.dominator.converter;

import com.empayre.dominator.dao.party.iface.TermSetHierarchyDao;
import com.empayre.dominator.data.WalletTermSetDataObject;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.exception.SerializationException;
import dev.vality.damsel.domain.IdentityProviderRef;
import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.dominator.WalletTermSet;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WalletTermSetConverter implements Converter<WalletTermSetDataObject, WalletTermSet> {

    private final TDeserializer deserializer;
    private final TermSetHierarchyDao termSetHierarchyDao;

    @Override
    public WalletTermSet convert(WalletTermSetDataObject source) {
        return new WalletTermSet()
                .setOwnerId(source.getPartyId())
                .setIdentityId(new IdentityProviderRef().setId(source.getIdentityId()))
                .setContractId(source.getContractId())
                .setWalletId(source.getWalletId())
                .setWalletName(source.getWalletName())
                .setTermSetName(source.getTermSetName())
                .setCurrency(source.getCurrency())
                .setCurrentTermSet(deserializeTermSet(source.getCurrentTermSetHierarchyObject()))
                .setTermSetHistory(deserializeTermSets(
                        termSetHierarchyDao.getTermSetHierarchyHistory(source.getTermSetId())));
    }

    private List<TermSetHierarchyObject> deserializeTermSets(List<TermSetHierarchy> termSetHierarchies) {
        return CollectionUtils.isEmpty(termSetHierarchies) ? new ArrayList<>() : termSetHierarchies.stream()
                .map(termSet -> deserializeTermSet(termSet.getTermSetHierarchyObject()))
                .toList();
    }

    private TermSetHierarchyObject deserializeTermSet(byte[] object) {
        try {
            if (object == null || object.length == 0) {
                return new TermSetHierarchyObject();
            }
            TermSetHierarchyObject termSetHierarchyObject = new TermSetHierarchyObject();
            deserializer.deserialize(termSetHierarchyObject, object);
            return termSetHierarchyObject;
        } catch (TException e) {
            throw new SerializationException(e);
        }
    }
}
