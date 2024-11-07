package dev.vality.dominator.dao.wallet.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.wallet.iface.WalletDao;
import dev.vality.dominator.domain.tables.pojos.Wallet;
import dev.vality.dominator.exception.DaoException;
import dev.vality.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.dominator.domain.tables.Wallet.WALLET;

@Component
public class WalletDaoImpl extends AbstractDao implements WalletDao {

    @Autowired
    public WalletDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Wallet wallet) throws DaoException {
        Long id = getDslContext()
                .insertInto(WALLET)
                .set(getDslContext().newRecord(WALLET, wallet))
                .onConflict(WALLET.WALLET_ID, WALLET.SEQUENCE_ID)
                .doUpdate()
                .set(getDslContext().newRecord(WALLET, wallet))
                .returning(WALLET.ID)
                .fetchOne()
                .getId();

        return Optional.ofNullable(id).map(Number::longValue);
    }

    @NotNull
    @Override
    public Wallet get(String walletId) throws DaoException {
        Wallet wallet = getDslContext()
                .selectFrom(WALLET)
                .where(WALLET.WALLET_ID.eq(walletId).and(WALLET.CURRENT))
                .fetchOneInto(Wallet.class);
        return Optional.ofNullable(wallet)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Wallet not found, walletId='%s'", walletId)));
    }

    @Override
    public void updateNotCurrent(Long walletId) throws DaoException {
        getDslContext()
                .update(WALLET)
                .set(WALLET.CURRENT, false)
                .where(WALLET.ID.eq(walletId).and(WALLET.CURRENT))
                .execute();
    }
}
