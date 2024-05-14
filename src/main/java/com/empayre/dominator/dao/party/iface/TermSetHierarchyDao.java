package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.exception.DaoException;

import java.util.List;

public interface TermSetHierarchyDao extends DomainObjectDao<TermSetHierarchy, Integer> {

    Long save(TermSetHierarchy termSetHierarchy) throws DaoException;

    void updateNotCurrent(Integer termSetHierarchyId) throws DaoException;

    List<TermSetHierarchy> getTermSetHierarchyHistory(Integer refId);
}
