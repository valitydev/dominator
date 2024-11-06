package dev.vality.dominator.converter;

import dev.vality.dominator.data.WalletTermSetDataObject;
import dev.vality.dominator.domain.tables.pojos.TermSetHierarchy;
import dev.vality.dominator.service.TermSetGettingService;
import dev.vality.damsel.domain.IdentityProviderRef;
import dev.vality.dominator.WalletTermSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static dev.vality.dominator.util.TermSetConverterUtils.replaceNull;

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
