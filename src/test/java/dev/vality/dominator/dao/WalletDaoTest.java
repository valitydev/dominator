package dev.vality.dominator.dao;

import dev.vality.dominator.config.PostgresqlSpringBootITest;
import dev.vality.dominator.dao.wallet.iface.WalletDao;
import dev.vality.dominator.domain.tables.pojos.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlSpringBootITest
public class WalletDaoTest {

    @Autowired
    private WalletDao walletDao;

    @Test
    public void walletDaoTest() {
        Wallet wallet = dev.vality.testcontainers.annotations.util.RandomBeans.random(Wallet.class);
        wallet.setCurrent(true);
        Long id = walletDao.save(wallet).get();
        wallet.setId(id);
        Wallet actual = walletDao.get(wallet.getWalletId());
        assertEquals(wallet, actual);
        walletDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        walletDao.save(wallet);
        assertNotNull(walletDao.get(wallet.getWalletId()));
    }
}
