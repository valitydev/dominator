package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.dao.dominant.iface.DomainObjectDao;
import dev.vality.dominator.domain.tables.pojos.TermSetHierarchy;
import dev.vality.dominator.exception.DaoException;

import java.util.List;

public interface TermSetHierarchyDao extends DomainObjectDao<TermSetHierarchy, Integer> {

    Long save(TermSetHierarchy termSetHierarchy) throws DaoException;

    void updateNotCurrent(Integer termSetHierarchyId) throws DaoException;

    TermSetHierarchy getCurrentTermSet(Integer refId);

    List<TermSetHierarchy> getTermSetHierarchyHistory(Integer refId, boolean current);

    byte[] getTermSetHierarchyObject(Integer refId, boolean current);
}
