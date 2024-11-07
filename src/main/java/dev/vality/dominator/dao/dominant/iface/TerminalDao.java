package dev.vality.dominator.dao.dominant.iface;

import dev.vality.dominator.domain.tables.pojos.Terminal;

import java.util.List;

public interface TerminalDao extends DomainObjectDao<Terminal, Integer> {

    List<Terminal> getTreminals(Integer terminalId);

}
