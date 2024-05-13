package com.empayre.dominator.dao.identity.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.identity.iface.IdentityDao;
import com.empayre.dominator.domain.tables.pojos.Identity;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static com.empayre.dominator.domain.tables.Identity.IDENTITY;

@Component
public class IdentityDaoImpl extends AbstractDao implements IdentityDao {

    @Autowired
    public IdentityDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Identity identity) throws DaoException {
        Long id = getDslContext()
                .insertInto(IDENTITY)
                .set(getDslContext().newRecord(IDENTITY, identity))
                .onConflict(IDENTITY.IDENTITY_ID, IDENTITY.SEQUENCE_ID)
                .doUpdate()
                .set(getDslContext().newRecord(IDENTITY, identity))
                .returning(IDENTITY.ID)
                .fetchOne()
                .getId();

        return Optional.ofNullable(id).map(Number::longValue);
    }

    @NotNull
    @Override
    public Identity get(String identityId) throws DaoException {
        Identity identity = getDslContext()
                .selectFrom(IDENTITY)
                .where(IDENTITY.IDENTITY_ID.eq(identityId)
                        .and(IDENTITY.CURRENT))
                .fetchOne()
                .into(Identity.class);

        return Optional.ofNullable(identity)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Identity not found, identityId='%s'", identityId)));
    }

    @Override
    public void updateNotCurrent(Long identityId) throws DaoException {
        getDslContext()
                .update(IDENTITY)
                .set(IDENTITY.CURRENT, false)
                .where(IDENTITY.ID.eq(identityId).and(IDENTITY.CURRENT))
                .execute();
    }
}
