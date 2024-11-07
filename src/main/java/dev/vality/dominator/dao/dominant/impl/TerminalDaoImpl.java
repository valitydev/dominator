package dev.vality.dominator.dao.dominant.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.dominant.iface.TerminalDao;
import dev.vality.dominator.domain.tables.pojos.Terminal;
import dev.vality.dominator.exception.DaoException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.util.List;

import static dev.vality.dominator.domain.Tables.TERMINAL;

@Component
public class TerminalDaoImpl extends AbstractDao implements TerminalDao {

    public TerminalDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(Terminal terminal) throws DaoException {
        return getDslContext()
                .insertInto(TERMINAL)
                .set(getDslContext().newRecord(TERMINAL, terminal))
                .returning(TERMINAL.ID)
                .fetchOne()
                .getId();
    }

    @Override
    public void updateNotCurrent(Integer terminalId) throws DaoException {
        getDslContext()
                .update(TERMINAL)
                .set(TERMINAL.CURRENT, false)
                .where(TERMINAL.TERMINAL_REF_ID.eq(terminalId)
                        .and(TERMINAL.CURRENT))
                .execute();
    }

    @Override
    public List<Terminal> getTreminals(Integer terminalId) {
        return getDslContext()
                .selectFrom(TERMINAL)
                .where(TERMINAL.TERMINAL_REF_ID.eq(terminalId))
                .fetchInto(Terminal.class);
    }
}
