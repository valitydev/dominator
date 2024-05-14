package com.empayre.dominator.dao.identity.iface;

import com.empayre.dominator.domain.tables.pojos.Identity;
import com.empayre.dominator.exception.DaoException;

import java.util.Optional;

public interface IdentityDao {

    Optional<Long> save(Identity identity) throws DaoException;

    Identity get(String identityId) throws DaoException;

    void updateNotCurrent(Long identityId) throws DaoException;

}
