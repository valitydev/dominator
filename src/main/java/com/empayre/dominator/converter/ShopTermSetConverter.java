package com.empayre.dominator.converter;

import com.empayre.dominator.data.ShopTermSetDataObject;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.service.TermSetGettingService;
import dev.vality.dominator.ShopTermSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.empayre.dominator.util.TermSetConverterUtils.replaceNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopTermSetConverter implements Converter<ShopTermSetDataObject, ShopTermSet> {

    private final TermSetGettingService termSetGettingService;

    @Override
    public ShopTermSet convert(ShopTermSetDataObject source) {
        Long contractRecordId = source.getContractRecordId();
        TermSetHierarchy currentTermSet =
                termSetGettingService.getCurrentTermSet(contractRecordId, source.getTermSetId());
        return new ShopTermSet()
                .setOwnerId(replaceNull(source.getPartyId()))
                .setShopId(replaceNull(source.getShopId()))
                .setContractId(replaceNull(source.getContractId()))
                .setShopName(replaceNull(source.getShopName()))
                .setTermSetName(replaceNull(currentTermSet.getName()))
                .setCurrency(replaceNull(source.getCurrency()))
                .setCurrentTermSet(
                        termSetGettingService.getTermSetFromObject(currentTermSet.getTermSetHierarchyObject()))
                .setTermSetHistory(termSetGettingService.getTermSetHistory(contractRecordId));
    }
}
