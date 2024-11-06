package dev.vality.dominator.dao.identity.iface;

import dev.vality.dominator.domain.tables.pojos.Identity;
import dev.vality.dominator.exception.DaoException;

import java.util.Optional;

public interface IdentityDao {

    Optional<Long> save(Identity identity) throws DaoException;

    Identity get(String identityId) throws DaoException;

    void updateNotCurrent(Long identityId) throws DaoException;

}
