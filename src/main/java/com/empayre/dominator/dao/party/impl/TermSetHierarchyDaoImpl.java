package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.TermSetHierarchyDao;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.exception.DaoException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.util.List;

import static com.empayre.dominator.domain.Tables.TERM_SET_HIERARCHY;

@Component
public class TermSetHierarchyDaoImpl extends AbstractDao implements TermSetHierarchyDao {

    public TermSetHierarchyDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(TermSetHierarchy termSetHierarchy) throws DaoException {
        return getDslContext()
                .insertInto(TERM_SET_HIERARCHY)
                .set(getDslContext().newRecord(TERM_SET_HIERARCHY, termSetHierarchy))
                .onConflict(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID, TERM_SET_HIERARCHY.VERSION_ID)
                .doUpdate()
                .set(getDslContext().newRecord(TERM_SET_HIERARCHY, termSetHierarchy))
                .returning(TERM_SET_HIERARCHY.ID)
                .fetchOne()
                .getId();
    }

    @Override
    public void updateNotCurrent(Integer termSetHierarchyId) throws DaoException {
        getDslContext()
                .update(TERM_SET_HIERARCHY)
                .set(TERM_SET_HIERARCHY.CURRENT, false)
                .where(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(termSetHierarchyId)
                        .and(TERM_SET_HIERARCHY.CURRENT))
                .execute();
    }

    @Override
    public TermSetHierarchy getCurrentTermSet(Integer refId) {
        return getDslContext()
                .selectFrom(TERM_SET_HIERARCHY)
                .where(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(refId))
                .and(TERM_SET_HIERARCHY.CURRENT.isTrue())
                .orderBy(TERM_SET_HIERARCHY.ID.desc())
                .fetchOneInto(TermSetHierarchy.class);
    }

    @Override
    public List<TermSetHierarchy> getTermSetHierarchyHistory(Integer refId, boolean current) {
        return getDslContext()
                .selectFrom(TERM_SET_HIERARCHY)
                .where(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(refId))
                .and(TERM_SET_HIERARCHY.CURRENT.eq(current))
                .orderBy(TERM_SET_HIERARCHY.ID.desc())
                .fetchInto(TermSetHierarchy.class);
    }

    public byte[] getTermSetHierarchyObject(Integer refId, boolean current) {
        return getDslContext()
                .select(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_OBJECT)
                .from(TERM_SET_HIERARCHY)
                .where(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(refId))
                .and(TERM_SET_HIERARCHY.CURRENT.eq(current))
                .fetchOne()
                .value1();
    }
}
