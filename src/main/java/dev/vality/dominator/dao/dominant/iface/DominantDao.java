package dev.vality.dominator.dao.dominant.iface;

import dev.vality.dominator.exception.DaoException;

public interface DominantDao {
    Long getLastVersionId() throws DaoException;

    void updateLastVersionId(Long versionId) throws DaoException;
}
