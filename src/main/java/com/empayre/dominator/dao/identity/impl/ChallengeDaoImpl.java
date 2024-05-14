package com.empayre.dominator.dao.identity.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.identity.iface.ChallengeDao;
import com.empayre.dominator.domain.tables.pojos.Challenge;
import com.empayre.dominator.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static com.empayre.dominator.domain.tables.Challenge.CHALLENGE;

@Component
public class ChallengeDaoImpl extends AbstractDao implements ChallengeDao {

    @Autowired
    public ChallengeDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Challenge challenge) throws DaoException {
        Long id = getDslContext()
                .insertInto(CHALLENGE)
                .set(getDslContext().newRecord(CHALLENGE, challenge))
                .onConflict(CHALLENGE.IDENTITY_ID, CHALLENGE.CHALLENGE_ID, CHALLENGE.SEQUENCE_ID)
                .doNothing()
                .returning(CHALLENGE.ID)
                .fetchOne()
                .getId();

        return Optional.ofNullable(id).map(Number::longValue);
    }

    @Override
    public Challenge get(String identityId, String challengeId) throws DaoException {
        return getDslContext()
                .selectFrom(CHALLENGE)
                .where(
                        CHALLENGE.IDENTITY_ID.eq(identityId)
                                .and(CHALLENGE.CHALLENGE_ID.eq(challengeId))
                                .and(CHALLENGE.CURRENT)
                )
                .fetchOne()
                .into(Challenge.class);
    }

    @Override
    public void updateNotCurrent(String identityId, Long challengeId) throws DaoException {
        getDslContext()
                .update(CHALLENGE)
                .set(CHALLENGE.CURRENT, false)
                .where(
                        CHALLENGE.ID.eq(challengeId)
                                .and(CHALLENGE.IDENTITY_ID.eq(identityId))
                                .and(CHALLENGE.CURRENT)
                )
                .execute();
    }
}
