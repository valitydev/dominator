package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.WalletTermSetDataObject;
import dev.vality.dominator.WalletSearchQuery;
import dev.vality.dominator.WalletTermSet;
import dev.vality.dominator.WalletTermSetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createWalletCondition;

@Component
@RequiredArgsConstructor
public class GetWalletTermSetsHandler implements GetTermSetsHandler<WalletSearchQuery, WalletTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<WalletTermSetDataObject, WalletTermSet> walletTermSetConverter;

    @Override
    public WalletTermSetsResponse handle(WalletSearchQuery query) {
        List<WalletTermSetDataObject> walletTermSets = termSetDao.getWalletTermSets(
                createWalletCondition(query),
                query.getCommonSearchQueryParams().getLimit()
        );

        return new WalletTermSetsResponse()
                .setTerms(walletTermSets.stream().map(set -> walletTermSetConverter.convert(set)).toList())
                .setContinuationToken(createContinuationToken(walletTermSets));
    }

    private String createContinuationToken(List<WalletTermSetDataObject> walletTermSets) {
        return CollectionUtils.isEmpty(walletTermSets)
                ? null : String.valueOf(walletTermSets.get(walletTermSets.size() - 1).getId());
    }
}
