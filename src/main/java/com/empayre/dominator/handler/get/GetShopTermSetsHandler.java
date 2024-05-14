package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.ShopTermSetDataObject;
import dev.vality.dominator.ShopSearchQuery;
import dev.vality.dominator.ShopTermSet;
import dev.vality.dominator.ShopTermSetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createShopCondition;

@Component
@RequiredArgsConstructor
public class GetShopTermSetsHandler implements GetTermSetsHandler<ShopSearchQuery, ShopTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<ShopTermSetDataObject, ShopTermSet> shopTermSetConverter;

    @Override
    public ShopTermSetsResponse handle(ShopSearchQuery query) {
        List<ShopTermSetDataObject> shopTermSets = termSetDao.getShopTermSets(
                createShopCondition(query),
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
}
