package com.empayre.dominator.converter;

import com.empayre.dominator.data.WalletTermSetDataObject;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.service.TermSetGettingService;
import dev.vality.damsel.domain.IdentityProviderRef;
import dev.vality.dominator.WalletTermSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.empayre.dominator.util.TermSetConverterUtils.replaceNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletTermSetConverter implements Converter<WalletTermSetDataObject, WalletTermSet> {

    private final TermSetGettingService termSetGettingService;

    @Override
    public WalletTermSet convert(WalletTermSetDataObject source) {
        Long contractRecordId = source.getContractRecordId();
        TermSetHierarchy currentTermSet =
                termSetGettingService.getCurrentTermSet(contractRecordId, source.getTermSetId());
        return new WalletTermSet()
                .setOwnerId(source.getPartyId())
                .setIdentityId(new IdentityProviderRef().setId(source.getIdentityId()))
                .setContractId(replaceNull(source.getContractId()))
                .setWalletId(replaceNull(source.getWalletId()))
                .setWalletName(replaceNull(source.getWalletName()))
                .setTermSetName(replaceNull(currentTermSet.getName()))
                .setCurrency(replaceNull(source.getCurrency()))
                .setCurrentTermSet(
                        termSetGettingService.getTermSetFromObject(currentTermSet.getTermSetHierarchyObject()))
                .setTermSetHistory(termSetGettingService.getTermSetHistory(contractRecordId));
    }
}
