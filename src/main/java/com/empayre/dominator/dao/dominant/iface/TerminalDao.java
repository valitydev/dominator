package com.empayre.dominator.dao.dominant.iface;

import com.empayre.dominator.domain.tables.pojos.Terminal;

import java.util.List;

public interface TerminalDao extends DomainObjectDao<Terminal, Integer> {

    List<Terminal> getTreminals(Integer terminalId);

}
