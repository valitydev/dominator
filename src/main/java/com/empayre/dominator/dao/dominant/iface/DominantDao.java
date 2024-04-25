package com.empayre.dominator.dao.dominant.iface;

import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

public interface DominantDao extends GenericDao {
    Long getLastVersionId() throws DaoException;

    void updateLastVersionId(Long versionId) throws DaoException;
}
