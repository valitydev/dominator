package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.TerminalSearchQuery;
import dev.vality.dominator.TerminalTermSet;
import dev.vality.dominator.TerminalTermSetsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createTerminalCondition;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTerminalTermSetsHandler implements GetTermSetsHandler<TerminalSearchQuery, TerminalTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<TerminalTermSetDataObject, TerminalTermSet> terminalTermSetConverter;

    @Override
    public TerminalTermSetsResponse handle(TerminalSearchQuery query) {
        log.info("Start TerminalTermSets getting (query: {})", query);
        List<TerminalTermSetDataObject> terminalTermSets = termSetDao.getTerminalTermSets(
                createTerminalCondition(query),
                query.getCommonSearchQueryParams().getLimit()
        );

        TerminalTermSetsResponse response = new TerminalTermSetsResponse()
                .setTerms(terminalTermSets.stream().map(set -> terminalTermSetConverter.convert(set)).toList())
                .setContinuationToken(createContinuationToken(terminalTermSets));
        log.info("Start TerminalTermSets getting (query: {}, terms.size: {}, token: {})",
                query, response.getTerms().size(), response.getContinuationToken());
        return response;
    }

    private String createContinuationToken(List<TerminalTermSetDataObject> terminalTermSets) {
        return CollectionUtils.isEmpty(terminalTermSets)
                ? null : String.valueOf(terminalTermSets.get(terminalTermSets.size() - 1).getId());
    }
}
