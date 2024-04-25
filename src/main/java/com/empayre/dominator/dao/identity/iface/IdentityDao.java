package com.empayre.dominator.dao.identity.iface;

import com.empayre.dominator.domain.tables.pojos.Identity;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

import java.util.Optional;

public interface IdentityDao extends GenericDao {

    Optional<Long> save(Identity identity) throws DaoException;

    Identity get(String identityId) throws DaoException;

    void updateNotCurrent(Long identityId) throws DaoException;

}
