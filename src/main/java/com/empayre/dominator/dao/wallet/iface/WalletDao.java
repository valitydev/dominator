package com.empayre.dominator.dao.wallet.iface;

import com.empayre.dominator.domain.tables.pojos.Wallet;
import com.empayre.dominator.exception.DaoException;
import dev.vality.dao.GenericDao;

import java.util.Optional;

public interface WalletDao extends GenericDao {

    Optional<Long> save(Wallet wallet) throws DaoException;

    Wallet get(String walletId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

}
