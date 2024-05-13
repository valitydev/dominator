package com.empayre.dominator.handler.get;

import com.empayre.dominator.dao.party.iface.TermSetDao;
import com.empayre.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.TerminalSearchQuery;
import dev.vality.dominator.TerminalTermSet;
import dev.vality.dominator.TerminalTermSetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.empayre.dominator.util.TermSetConditionUtils.createTerminalCondition;

@Component
@RequiredArgsConstructor
public class GetTerminalTermSetsHandler implements GetTermSetsHandler<TerminalSearchQuery, TerminalTermSetsResponse> {

    private final TermSetDao termSetDao;
    private final Converter<TerminalTermSetDataObject, TerminalTermSet> terminalTermSetConverter;

    @Override
    public TerminalTermSetsResponse handle(TerminalSearchQuery query) {
        List<TerminalTermSetDataObject> terminalTermSets = termSetDao.getTerminalTermSets(
                createTerminalCondition(query),
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
}
