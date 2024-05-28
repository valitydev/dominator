package com.empayre.dominator.converter;

import com.empayre.dominator.dao.dominant.iface.TerminalDao;
import com.empayre.dominator.data.TerminalTermSetDataObject;
import com.empayre.dominator.domain.tables.pojos.Terminal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.damsel.domain.ProviderRef;
import dev.vality.damsel.domain.ProvisionTermSet;
import dev.vality.damsel.domain.TerminalRef;
import dev.vality.dominator.ProvisionTermSetHistory;
import dev.vality.dominator.TerminalTermSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.empayre.dominator.util.TermSetConverterUtils.replaceNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class TerminalTermSetConverter implements Converter<TerminalTermSetDataObject, TerminalTermSet> {

    private final TDeserializer deserializer;
    private final TerminalDao terminalDao;
    private static final String DELIMITER = ", ";

    @Override
    public TerminalTermSet convert(TerminalTermSetDataObject source) {
        return new TerminalTermSet()
                .setTerminalId(new TerminalRef(source.getTerminalId()))
                .setTerminalName(replaceNull(source.getTerminalName()))
                .setProviderId(new ProviderRef(source.getProviderId()))
                .setProviderName(replaceNull(source.getProviderName()))
                .setCurrency(replaceNull(getCurrency(source.getAccountsJson())))
                .setCurrentTermSet(deserializeTermSet(source.getTermSetObject()))
                .setTermSetHistory(deserializeTermSets(terminalDao.getTreminals(source.getTerminalId())));
    }

    private List<ProvisionTermSetHistory> deserializeTermSets(List<Terminal> termSetHierarchies) {
        return CollectionUtils.isEmpty(termSetHierarchies) ? new ArrayList<>() : termSetHierarchies.stream()
                .map(terminal -> new ProvisionTermSetHistory()
                        .setTermSet(deserializeTermSet(terminal.getTermsObject())))
                .toList();
    }

    private ProvisionTermSet deserializeTermSet(byte[] object) {
        try {
            if (object == null || object.length == 0) {
                return new ProvisionTermSet();
            }
            ProvisionTermSet termSet = new ProvisionTermSet();
            deserializer.deserialize(termSet, object);
            return termSet;
        } catch (TException e) {
            log.error("ProvisionTermSet deserialization exception", e);
            return new ProvisionTermSet();
        }
    }

    private String getCurrency(String accountsJson) {
        try {
            Map<String, Long> map = new ObjectMapper().readValue(accountsJson, HashMap.class);
            return map.keySet().stream()
                    .collect(Collectors.joining(DELIMITER));
        } catch (JsonProcessingException e) {
            log.error("Currency extraction error", e);
            return Strings.EMPTY;
        }
    }
}
