package dev.vality.dominator.dao.wallet.iface;

import dev.vality.dominator.domain.tables.pojos.Wallet;
import dev.vality.dominator.exception.DaoException;

import java.util.Optional;

public interface WalletDao {

    Optional<Long> save(Wallet wallet) throws DaoException;

    Wallet get(String walletId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

}
