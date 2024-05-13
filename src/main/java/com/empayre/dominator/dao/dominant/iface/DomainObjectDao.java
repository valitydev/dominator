package com.empayre.dominator.dao.dominant.iface;

import com.empayre.dominator.exception.DaoException;

public interface DomainObjectDao<T, I> {

    Long save(T domainObject) throws DaoException;

    void updateNotCurrent(I objectId) throws DaoException;
}
