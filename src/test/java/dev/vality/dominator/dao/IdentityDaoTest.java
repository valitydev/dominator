package dev.vality.dominator.dao;

import dev.vality.dominator.config.PostgresqlSpringBootITest;
import dev.vality.dominator.dao.identity.iface.IdentityDao;
import dev.vality.dominator.domain.tables.pojos.Identity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

@PostgresqlSpringBootITest
public class IdentityDaoTest {

    @Autowired
    private IdentityDao identityDao;

    @Test
    public void identityDaoTest() {
        Identity identity = dev.vality.testcontainers.annotations.util.RandomBeans.random(Identity.class);
        identity.setCurrent(true);
        Long id = identityDao.save(identity).get();
        identity.setId(id);
        Identity actual = identityDao.get(identity.getIdentityId());
        Assertions.assertEquals(identity, actual);
        identityDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        identityDao.save(identity);

        Identity resultIdentity = identityDao.get(identity.getIdentityId());
        assertNotNull(resultIdentity);
    }
}
