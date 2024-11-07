package dev.vality.dominator.dao.identity.iface;

import dev.vality.dominator.domain.tables.pojos.Challenge;
import dev.vality.dominator.exception.DaoException;

import java.util.Optional;

public interface ChallengeDao {

    Optional<Long> save(Challenge challenge) throws DaoException;

    Challenge get(String identityId, String challengeId) throws DaoException;

    void updateNotCurrent(String identityId, Long challengeId) throws DaoException;

}
