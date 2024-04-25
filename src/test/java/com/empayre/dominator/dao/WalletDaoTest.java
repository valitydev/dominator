package com.empayre.dominator.dao;

import com.empayre.dominator.config.PostgresqlSpringBootITest;
import com.empayre.dominator.dao.wallet.iface.WalletDao;
import com.empayre.dominator.domain.tables.pojos.Wallet;
import com.empayre.dominator.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class WalletDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WalletDao walletDao;

    @Test
    public void walletDaoTest() {
        jdbcTemplate.execute("truncate table dmn.wallet cascade");
        Wallet wallet = dev.vality.testcontainers.annotations.util.RandomBeans.random(Wallet.class);
        wallet.setCurrent(true);
        Long id = walletDao.save(wallet).get();
        wallet.setId(id);
        Wallet actual = walletDao.get(wallet.getWalletId());
        Assertions.assertEquals(wallet, actual);
        walletDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        walletDao.save(wallet);

        assertThrows(NotFoundException.class, () -> walletDao.get(wallet.getWalletId()));
    }

}
