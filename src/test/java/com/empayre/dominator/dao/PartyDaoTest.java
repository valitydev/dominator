package com.empayre.dominator.dao;

import com.empayre.dominator.config.PostgresqlJooqSpringBootITest;
import com.empayre.dominator.dao.party.iface.PartyDao;
import com.empayre.dominator.dao.party.impl.PartyDaoImpl;
import com.empayre.dominator.domain.tables.pojos.Party;
import com.empayre.dominator.domain.tables.records.PartyRecord;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.empayre.dominator.domain.tables.Party.PARTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqSpringBootITest
@ContextConfiguration(classes = {PartyDaoImpl.class})
class PartyDaoTest {

    @Autowired
    PartyDao partyDao;

    @Autowired
    DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(PARTY).execute();
    }

    @Test
    void save() {
        Party party = RandomBeans.random(Party.class);

        partyDao.save(party);

        PartyRecord partyRecord = dslContext.fetchAny(PARTY);

        assertNotNull(partyRecord);
        assertEquals(party.getPartyId(), partyRecord.getPartyId());
        assertEquals(party.getChangeId(), partyRecord.getChangeId());
        assertEquals(party.getName(), partyRecord.getName());

    }
}