package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.ShopTermSetDataObject;
import dev.vality.dominator.ShopSearchQuery;
import dev.vality.dominator.ShopTermSet;
import dev.vality.dominator.ShopTermSetsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createShopCondition;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetShopTermSetsHandler implements GetTermSetsHandler<ShopSearchQuery, ShopTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<ShopTermSetDataObject, ShopTermSet> shopTermSetConverter;

    @Override
    public ShopTermSetsResponse handle(ShopSearchQuery query) {
        log.info("Start ShopTermSets getting (query: {})", query);
        List<ShopTermSetDataObject> shopTermSets = termSetDao.getShopTermSets(
                createShopCondition(query),
                query.getCommonSearchQueryParams().getLimit()
        );

        ShopTermSetsResponse response = new ShopTermSetsResponse()
                .setTerms(shopTermSets.stream().map(termSet -> shopTermSetConverter.convert(termSet)).toList())
                .setContinuationToken(createContinuationToken(shopTermSets));
        log.info("Finish ShopTermSets getting (query: {}, terms.size: {}, token: {})",
                query, response.getTerms().size(), response.getContinuationToken());
        log.debug("Result ShopTermSetsResponse: {}", response);
        return response;
    }

    private String createContinuationToken(List<ShopTermSetDataObject> shopTermSets) {
        return CollectionUtils.isEmpty(shopTermSets)
                ? null : String.valueOf(shopTermSets.get(shopTermSets.size() - 1).getId());
    }
}
