package dev.vality.dominator.handler.dominant.impl;

import dev.vality.dominator.exception.SerializationException;
import dev.vality.dominator.handler.dominant.AbstractDominantHandler;
import dev.vality.damsel.domain.TerminalObject;
import dev.vality.dominator.dao.dominant.iface.DomainObjectDao;
import dev.vality.dominator.dao.dominant.impl.TerminalDaoImpl;
import dev.vality.dominator.domain.tables.pojos.Terminal;
import dev.vality.dominator.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TerminalHandler extends AbstractDominantHandler<TerminalObject, Terminal, Integer> {

    private final TerminalDaoImpl terminalDao;
    private final TSerializer serializer;

    @Override
    protected DomainObjectDao<Terminal, Integer> getDomainObjectDao() {
        return terminalDao;
    }

    @Override
    protected TerminalObject getTargetObject() {
        return getDomainObject().getTerminal();
    }

    @Override
    protected Integer getTargetObjectRefId() {
        return getTargetObject().getRef().getId();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetTerminal();
    }

    @Override
    public Terminal convertToDatabaseObject(TerminalObject terminalObject,
                                            Long versionId,
                                            LocalDateTime createdAt,
                                            boolean current) {
        Terminal terminal = new Terminal();
        terminal.setVersionId(versionId);
        terminal.setCreatedAt(createdAt);
        terminal.setTerminalRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.Terminal data = terminalObject.getData();
        terminal.setName(data.getName());
        terminal.setDescription(data.getDescription());
        if (data.isSetRiskCoverage()) {
            terminal.setRiskCoverage(data.getRiskCoverage().name());
        }
        if (data.isSetTerms()) {
            terminal.setTermsJson(JsonUtil.thriftBaseToJsonString(data.getTerms()));
            try {
                terminal.setTermsObject(serializer.serialize(data.getTerms()));
            } catch (TException e) {
                throw new SerializationException(e);
            }
        }
        terminal.setExternalTerminalId(data.getExternalTerminalId());
        terminal.setExternalMerchantId(data.getExternalMerchantId());
        terminal.setMcc(data.getMcc());
        if (data.isSetProviderRef()) {
            terminal.setTerminalProviderRefId(data.getProviderRef().getId());
        }
        terminal.setCurrent(current);
        return terminal;
    }
}
