package dev.vality.dominator.dao.dominant.iface;

import dev.vality.dominator.exception.DaoException;

public interface DomainObjectDao<T, I> {

    Long save(T domainObject) throws DaoException;

    void updateNotCurrent(I objectId) throws DaoException;
}
