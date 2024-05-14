package com.empayre.dominator.dao.dominant.iface;

import com.empayre.dominator.exception.DaoException;

public interface DominantDao {
    Long getLastVersionId() throws DaoException;

    void updateLastVersionId(Long versionId) throws DaoException;
}
