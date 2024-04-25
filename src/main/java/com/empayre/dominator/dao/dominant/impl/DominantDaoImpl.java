package com.empayre.dominator.dao.dominant.impl;

import com.empayre.dominator.dao.dominant.iface.DominantDao;
import com.empayre.dominator.domain.Tables;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.impl.AbstractGenericDao;

import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Component
public class DominantDaoImpl extends AbstractGenericDao implements DominantDao {

    public DominantDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long getLastVersionId() throws DaoException {
        Query query = getDslContext()
                .select(Tables.DOMINANT_LAST_VERSION_ID.VERSION_ID)
                .from(Tables.DOMINANT_LAST_VERSION_ID);
        return fetchOne(query, Long.class);
    }

    @Override
    public void updateLastVersionId(Long versionId) throws DaoException {
        Query query = getDslContext()
                .update(Tables.DOMINANT_LAST_VERSION_ID)
                .set(Tables.DOMINANT_LAST_VERSION_ID.VERSION_ID, versionId)
                .set(Tables.DOMINANT_LAST_VERSION_ID.WTIME, LocalDateTime.now());
        executeOne(query);
    }
}
