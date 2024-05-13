package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.WalletTermSetDataObject;
import dev.vality.dominator.*;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.empayre.dominator.domain.Tables.*;

@Component
@RequiredArgsConstructor
public class GetWalletTermSetsHandler implements GetTermSetsHandler<WalletSearchQuery, WalletTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<WalletTermSetDataObject, WalletTermSet> walletTermSetConverter;

    @Override
    public WalletTermSetsResponse handle(WalletSearchQuery query) {
        List<WalletTermSetDataObject> walletTermSets = termSetDao.getWalletTermSets(
                createCondition(query),
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

    private Condition createCondition(WalletSearchQuery query) {
        Condition condition = WALLET.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            condition = condition.and(WALLET.CURRENCY_CODE.in(commonSearchQueryParams.getCurrencies()));
        }
        if (StringUtils.hasText(query.getPartyId())) {
            condition = condition.and(WALLET.PARTY_ID.eq(query.getPartyId()));
        }
        if (!CollectionUtils.isEmpty(query.getIdentityIds())) {
            List<String> identitiesIds = query.getIdentityIds().stream()
                    .map(identity -> identity.getId())
                    .toList();
            condition = condition.and(WALLET.IDENTITY_ID.in(identitiesIds));
        }
        if (!CollectionUtils.isEmpty(query.getWalletIds())) {
            condition = condition.and(WALLET.WALLET_ID.in(query.getWalletIds()));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsIds())) {
            List<Integer> ids = query.getTermSetsIds().stream()
                    .map(id -> id.getId())
                    .toList();
            condition = condition.and(CONTRACT.TERMS_ID.in(ids));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsNames())) {
            condition = condition.and(TERM_SET_HIERARCHY.NAME.in(query.getTermSetsNames()));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(WALLET.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }
}
