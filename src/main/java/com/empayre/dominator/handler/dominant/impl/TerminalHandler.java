package com.empayre.dominator.handler.dominant.impl;

import com.empayre.dominator.handler.dominant.AbstractDominantHandler;
import dev.vality.damsel.domain.TerminalObject;
import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.dao.dominant.impl.TerminalDaoImpl;
import com.empayre.dominator.domain.tables.pojos.Terminal;
import com.empayre.dominator.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TerminalHandler extends AbstractDominantHandler<TerminalObject, Terminal, Integer> {

    private final TerminalDaoImpl terminalDao;

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
    public Terminal convertToDatabaseObject(TerminalObject terminalObject, Long versionId, boolean current) {
        Terminal terminal = new Terminal();
        terminal.setVersionId(versionId);
        terminal.setTerminalRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.Terminal data = terminalObject.getData();
        terminal.setName(data.getName());
        terminal.setDescription(data.getDescription());
        if (data.isSetRiskCoverage()) {
            terminal.setRiskCoverage(data.getRiskCoverage().name());
        }
        if (data.isSetTerms()) {
            terminal.setTermsJson(JsonUtil.thriftBaseToJsonString(data.getTerms()));
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
