package com.empayre.dominator.dao.identity.iface;

import com.empayre.dominator.domain.tables.pojos.Challenge;
import com.empayre.dominator.exception.DaoException;

import java.util.Optional;

public interface ChallengeDao {

    Optional<Long> save(Challenge challenge) throws DaoException;

    Challenge get(String identityId, String challengeId) throws DaoException;

    void updateNotCurrent(String identityId, Long challengeId) throws DaoException;

}
