package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.TerminalTermSetDataObject;
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
public class GetTerminalTermSetsHandler implements GetTermSetsHandler<TerminalSearchQuery, TerminalTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<TerminalTermSetDataObject, TerminalTermSet> terminalTermSetConverter;

    @Override
    public TerminalTermSetsResponse handle(TerminalSearchQuery query) {
        List<TerminalTermSetDataObject> terminalTermSets = termSetDao.getTerminalTermSets(
                createCondition(query),
                query.getCommonSearchQueryParams().getLimit()
        );

        return new TerminalTermSetsResponse()
                .setTerms(terminalTermSets.stream().map(set -> terminalTermSetConverter.convert(set)).toList())
                .setContinuationToken(createContinuationToken(terminalTermSets));
    }

    private String createContinuationToken(List<TerminalTermSetDataObject> terminalTermSets) {
        return CollectionUtils.isEmpty(terminalTermSets)
                ? null : String.valueOf(terminalTermSets.get(terminalTermSets.size() - 1).getId());
    }

    private Condition createCondition(TerminalSearchQuery query) {
        Condition condition = TERMINAL.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            Condition currencyCondition = null;
            for (String currency : commonSearchQueryParams.getCurrencies()) {
                Condition currentCurrencyCondition = PROVIDER.ACCOUNTS_JSON.like("%%s%".formatted(currency));
                currencyCondition = currencyCondition == null
                        ? currentCurrencyCondition : currencyCondition.or(currentCurrencyCondition);

            }
            condition = condition.and(currencyCondition);
        }
        if (!CollectionUtils.isEmpty(query.getTerminalIds())) {
            List<Integer> ids = query.getTerminalIds().stream()
                    .map(refId -> refId.getId())
                    .toList();
            condition = condition.and(TERMINAL.TERMINAL_REF_ID.in(ids));
        }
        if (!CollectionUtils.isEmpty(query.getProviderIds())) {
            List<Integer> ids = query.getProviderIds().stream()
                    .map(refId -> refId.getId())
                    .toList();
            condition = condition.and(PROVIDER.PROVIDER_REF_ID.in(ids));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(TERMINAL.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }
}
