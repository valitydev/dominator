package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.ShopTermSetDataObject;
import dev.vality.dominator.CommonSearchQueryParams;
import dev.vality.dominator.ShopSearchQuery;
import dev.vality.dominator.ShopTermSet;
import dev.vality.dominator.ShopTermSetsResponse;
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
public class GetShopTermSetsHandler implements GetTermSetsHandler<ShopSearchQuery, ShopTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<ShopTermSetDataObject, ShopTermSet> shopTermSetConverter;

    @Override
    public ShopTermSetsResponse handle(ShopSearchQuery query) {
        List<ShopTermSetDataObject> shopTermSets = termSetDao.getShopTermSets(
                createCondition(query),
                query.getCommonSearchQueryParams().getLimit()
        );

        return new ShopTermSetsResponse()
                .setTerms(shopTermSets.stream().map(termSet -> shopTermSetConverter.convert(termSet)).toList())
                .setContinuationToken(createContinuationToken(shopTermSets));
    }

    private String createContinuationToken(List<ShopTermSetDataObject> shopTermSets) {
        return CollectionUtils.isEmpty(shopTermSets)
                ? null : String.valueOf(shopTermSets.get(shopTermSets.size() - 1).getId());
    }

    private Condition createCondition(ShopSearchQuery query) {
        Condition condition = SHOP.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            condition = condition.and(SHOP.ACCOUNT_CURRENCY_CODE.in(commonSearchQueryParams.getCurrencies()));
        }
        if (StringUtils.hasText(query.getPartyId())) {
            condition = condition.and(SHOP.PARTY_ID.eq(query.getPartyId()));
        }
        if (!CollectionUtils.isEmpty(query.getShopIds())) {
            condition = condition.and(SHOP.SHOP_ID.in(query.getShopIds()));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsIds())) {
            List<Integer> termSetIds = query.getTermSetsIds().stream()
                    .map(term -> term.getId())
                    .toList();
            condition = condition.and(CONTRACT.TERMS_ID.in(termSetIds));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsNames())) {
            condition = condition.and(TERM_SET_HIERARCHY.NAME.in(query.getTermSetsNames()));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(SHOP.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }
}
