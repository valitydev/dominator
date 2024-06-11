package com.empayre.dominator.service;

import com.empayre.dominator.config.SerializationConfig;
import com.empayre.dominator.dao.party.iface.PartyDao;
import com.empayre.dominator.factory.machine.event.PartyMachineEventCopyFactoryImpl;
import com.empayre.dominator.handler.event.stock.impl.partymngmnt.party.PartyCreatedHandler;
import dev.vality.damsel.domain.PartyContactInfo;
import dev.vality.damsel.payment_processing.PartyChange;
import dev.vality.damsel.payment_processing.PartyCreated;
import dev.vality.damsel.payment_processing.PartyEventData;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.sink.common.serialization.impl.PartyEventDataSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PartyMachineEventCopyFactoryImpl.class,
        PartyCreatedHandler.class, SerializationConfig.class, PartyManagementService.class})
class PartyManagementServiceTest {

    @Autowired
    PartyManagementService partyManagementService;

    @MockBean
    PartyDao partyDao;

    @BeforeEach
    public void setUp() {
        when(partyDao.save(any())).thenReturn(Optional.of(1L));
    }

    @Test
    void handleEvents() {
        List<MachineEvent> machineEvents = new ArrayList<>();
        machineEvents.add(createMessage());
        partyManagementService.handleEvents(machineEvents);

        Mockito.verify(partyDao, times(1)).save(any());
    }

    private MachineEvent createMessage() {
        PartyEventData partyEventData = new PartyEventData();
        ArrayList<PartyChange> changes = new ArrayList<>();
        PartyChange partyChange = new PartyChange();
        partyChange.setPartyCreated(new PartyCreated()
                .setContactInfo(new PartyContactInfo()
                        .setRegistrationEmail("test@mail.ru"))
                .setCreatedAt(Instant.now().toString())
                .setId("test"));
        changes.add(partyChange);
        partyEventData.setChanges(changes);
        PartyEventDataSerializer partyEventDataSerializer = new PartyEventDataSerializer();
        Value data = new Value();
        data.setBin(partyEventDataSerializer.serialize(partyEventData));
        MachineEvent message = new MachineEvent();
        message.setCreatedAt(Instant.now().toString());
        message.setEventId(1L);
        message.setSourceNs("sad");
        message.setSourceId("sda");
        message.setData(data);
        return message;
    }
}
