package dev.vality.dominator.handler.event.impl.partymngmnt.party;

import dev.vality.dominator.TestData;
import dev.vality.dominator.config.PostgresqlSpringBootITest;
import dev.vality.dominator.dao.party.iface.PartyDao;
import dev.vality.dominator.domain.tables.pojos.Party;
import dev.vality.dominator.handler.event.stock.impl.partymngmnt.party.PartyModificationAdditionalInfoHandler;
import dev.vality.damsel.payment_processing.PartyChange;
import dev.vality.damsel.payment_processing.PartyEventData;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@PostgresqlSpringBootITest
class PartyModificationAdditionalInfoHandlerTest {

    @Autowired
    PartyModificationAdditionalInfoHandler handler;

    @Autowired
    PartyDao partyDao;

    @Test
    void handle() {
        String partyName = "partyName";
        String comment = "comment";
        String email = "test@mail";
        Party party = RandomBeans.random(Party.class);
        party.setCurrent(Boolean.TRUE);
        partyDao.save(party);
        PartyChange partyChange =
                TestData.createPartyChangeWithPartyModificationAdditionalInfo(partyName, comment, email);
        PartyEventData partyEventData = new PartyEventData();
        partyEventData.setChanges(List.of(partyChange));
        MachineEvent machineEvent = TestData.createPartyEventDataMachineEvent(partyEventData, party.getPartyId());

        handler.handle(partyChange, machineEvent, 1);

        Party partyResult = partyDao.get(party.getPartyId());
        assertNotNull(partyResult);
        assertEquals(partyName, partyResult.getName());
        assertEquals(comment, partyResult.getComment());
        assertThat(partyResult.getManagerContactEmails(), containsString(email));
    }
}
