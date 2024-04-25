package com.empayre.dominator.dao.identity.impl;

import com.empayre.dominator.dao.identity.iface.IdentityDao;
import com.empayre.dominator.domain.tables.pojos.Identity;
import com.empayre.dominator.domain.tables.records.IdentityRecord;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import static com.empayre.dominator.domain.tables.Identity.IDENTITY;

@Component
public class IdentityDaoImpl extends AbstractGenericDao implements IdentityDao {

    private final RowMapper<Identity> identityRowMapper;

    @Autowired
    public IdentityDaoImpl(DataSource dataSource) {
        super(dataSource);
        identityRowMapper = new RecordRowMapper<>(IDENTITY, Identity.class);
    }

    @Override
    public Optional<Long> save(Identity identity) throws DaoException {
        IdentityRecord record = getDslContext().newRecord(IDENTITY, identity);
        Query query = getDslContext()
                .insertInto(IDENTITY)
                .set(record)
                .onConflict(IDENTITY.IDENTITY_ID, IDENTITY.SEQUENCE_ID)
                .doNothing()
                .returning(IDENTITY.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Identity get(String identityId) throws DaoException {
        Query query = getDslContext()
                .selectFrom(IDENTITY)
                .where(IDENTITY.IDENTITY_ID.eq(identityId)
                        .and(IDENTITY.CURRENT));

        return Optional.ofNullable(fetchOne(query, identityRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Identity not found, identityId='%s'", identityId)));
    }

    @Override
    public void updateNotCurrent(Long identityId) throws DaoException {
        Query query = getDslContext()
                .update(IDENTITY)
                .set(IDENTITY.CURRENT, false)
                .where(IDENTITY.ID.eq(identityId).and(IDENTITY.CURRENT));
        execute(query);
    }
}
