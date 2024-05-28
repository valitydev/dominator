package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.WalletTermSetDataObject;
import dev.vality.dominator.WalletSearchQuery;
import dev.vality.dominator.WalletTermSet;
import dev.vality.dominator.WalletTermSetsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createWalletCondition;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetWalletTermSetsHandler implements GetTermSetsHandler<WalletSearchQuery, WalletTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<WalletTermSetDataObject, WalletTermSet> walletTermSetConverter;

    @Override
    public WalletTermSetsResponse handle(WalletSearchQuery query) {
        log.info("Start WalletTermSets getting (query: {})", query);
        int limit = query.getCommonSearchQueryParams().getLimit();
        List<WalletTermSetDataObject> walletTermSets =
                termSetDao.getWalletTermSets(createWalletCondition(query), limit);

        WalletTermSetsResponse response = new WalletTermSetsResponse()
                .setTerms(walletTermSets.stream().map(set -> walletTermSetConverter.convert(set)).toList())
                .setContinuationToken(createContinuationToken(walletTermSets, limit));
        log.info("Finish WalletTermSets getting (query: {}, terms.size: {}, token: {})",
                query, response.getTerms().size(), response.getContinuationToken());
        log.debug("Result WalletTermSetsResponse: {}", response);
        return response;
    }

    private String createContinuationToken(List<WalletTermSetDataObject> walletTermSets, int limit) {
        return CollectionUtils.isEmpty(walletTermSets) || walletTermSets.size() < limit
                ? null : String.valueOf(walletTermSets.get(walletTermSets.size() - 1).getId());
    }
}
