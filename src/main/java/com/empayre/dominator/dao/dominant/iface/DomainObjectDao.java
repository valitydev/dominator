package com.empayre.dominator.dao.dominant.iface;

import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

public interface DomainObjectDao<T, I> extends GenericDao {

    Long save(T domainObject) throws DaoException;

    void updateNotCurrent(I objectId) throws DaoException;
}
