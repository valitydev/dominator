package dev.vality.dominator.converter;

import dev.vality.dominator.dao.dominant.iface.TerminalDao;
import dev.vality.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.domain.tables.pojos.Terminal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.damsel.domain.ProviderRef;
import dev.vality.damsel.domain.ProvisionTermSet;
import dev.vality.damsel.domain.TerminalRef;
import dev.vality.dominator.ProvisionTermSetHistory;
import dev.vality.dominator.TerminalTermSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.vality.dominator.util.TermSetConverterUtils.replaceNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class TerminalTermSetConverter implements Converter<TerminalTermSetDataObject, TerminalTermSet> {

    private final TDeserializer deserializer;
    private final TerminalDao terminalDao;

    @Override
    public TerminalTermSet convert(TerminalTermSetDataObject source) {
        return new TerminalTermSet()
                .setTerminalId(new TerminalRef(source.getTerminalId()))
                .setTerminalName(replaceNull(source.getTerminalName()))
                .setProviderId(new ProviderRef(source.getProviderId()))
                .setProviderName(replaceNull(source.getProviderName()))
                .setCurrencies(getCurrencies(source.getAccountsJson()))
                .setCurrentTermSet(deserializeTermSet(source.getTermSetObject()))
                .setTermSetHistory(deserializeTermSets(terminalDao.getTreminals(source.getTerminalId())));
    }

    private List<ProvisionTermSetHistory> deserializeTermSets(List<Terminal> termSetHierarchies) {
        return CollectionUtils.isEmpty(termSetHierarchies) ? new ArrayList<>() : termSetHierarchies.stream()
                .map(terminal -> new ProvisionTermSetHistory()
                        .setTermSet(deserializeTermSet(terminal.getTermsObject()))
                        .setAppliedAt(terminal.getCreatedAt() == null ? null : terminal.getCreatedAt().toString()))
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

    private List<String> getCurrencies(String accountsJson) {
        try {
            Map<String, Long> map = new ObjectMapper().readValue(accountsJson, HashMap.class);
            return new ArrayList<>(map.keySet());
        } catch (JsonProcessingException e) {
            log.error("Currency extraction error", e);
            return new ArrayList<>();
        }
    }
}
