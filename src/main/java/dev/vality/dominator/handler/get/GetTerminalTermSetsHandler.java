package dev.vality.dominator.handler.get;

import dev.vality.dominator.dao.party.iface.TermSetDao;
import dev.vality.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.TerminalSearchQuery;
import dev.vality.dominator.TerminalTermSet;
import dev.vality.dominator.TerminalTermSetsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static dev.vality.dominator.util.TermSetConditionUtils.createTerminalCondition;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTerminalTermSetsHandler implements GetTermSetsHandler<TerminalSearchQuery, TerminalTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<TerminalTermSetDataObject, TerminalTermSet> terminalTermSetConverter;

    @Override
    public TerminalTermSetsResponse handle(TerminalSearchQuery query) {
        log.info("Start TerminalTermSets getting (query: {})", query);
        int limit = query.getCommonSearchQueryParams().getLimit();
        List<TerminalTermSetDataObject> terminalTermSets =
                termSetDao.getTerminalTermSets(createTerminalCondition(query), limit);

        TerminalTermSetsResponse response = new TerminalTermSetsResponse()
                .setTerms(terminalTermSets.stream().map(set -> terminalTermSetConverter.convert(set)).toList())
                .setContinuationToken(createContinuationToken(terminalTermSets, limit));
        log.info("Finish TerminalTermSets getting (query: {}, terms.size: {}, token: {})",
                query, response.getTerms().size(), response.getContinuationToken());
        log.debug("Result TerminalTermSetsResponse: {}", response);
        return response;
    }

    private String createContinuationToken(List<TerminalTermSetDataObject> terminalTermSets, int limit) {
        return CollectionUtils.isEmpty(terminalTermSets) || terminalTermSets.size() < limit
                ? null : String.valueOf(terminalTermSets.get(terminalTermSets.size() - 1).getId());
    }
}
