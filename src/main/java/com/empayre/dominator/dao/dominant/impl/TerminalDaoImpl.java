package com.empayre.dominator.dao.dominant.impl;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.impl.AbstractGenericDao;
import com.empayre.dominator.domain.Tables;
import com.empayre.dominator.domain.tables.pojos.Terminal;
import com.empayre.dominator.domain.tables.records.TerminalRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TerminalDaoImpl extends AbstractGenericDao implements DomainObjectDao<Terminal, Integer> {

    public TerminalDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(Terminal terminal) throws DaoException {
        TerminalRecord terminalRecord = getDslContext().newRecord(Tables.TERMINAL, terminal);
        Query query = getDslContext()
                .insertInto(Tables.TERMINAL)
                .set(terminalRecord)
                .returning(Tables.TERMINAL.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        executeOne(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNotCurrent(Integer terminalId) throws DaoException {
        Query query = getDslContext()
                .update(Tables.TERMINAL)
                .set(Tables.TERMINAL.CURRENT, false)
                .where(Tables.TERMINAL.TERMINAL_REF_ID.eq(terminalId).and(Tables.TERMINAL.CURRENT));
        executeOne(query);
    }
}
