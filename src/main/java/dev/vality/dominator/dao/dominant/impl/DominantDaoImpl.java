package dev.vality.dominator.dao.dominant.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.dominant.iface.DominantDao;
import dev.vality.dominator.domain.Tables;
import dev.vality.dominator.exception.DaoException;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Component
public class DominantDaoImpl extends AbstractDao implements DominantDao {

    public DominantDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long getLastVersionId() throws DaoException {
        return getDslContext()
                .select(Tables.DOMINANT_LAST_VERSION_ID.VERSION_ID)
                .from(Tables.DOMINANT_LAST_VERSION_ID)
                .fetchOne()
                .value1();
    }

    @Override
    public void updateLastVersionId(Long versionId) throws DaoException {
        getDslContext()
                .update(Tables.DOMINANT_LAST_VERSION_ID)
                .set(Tables.DOMINANT_LAST_VERSION_ID.VERSION_ID, versionId)
                .set(Tables.DOMINANT_LAST_VERSION_ID.WTIME, LocalDateTime.now()).execute();
    }
}
