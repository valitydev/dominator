package com.empayre.dominator;

import dev.vality.damsel.domain.*;
import dev.vality.damsel.payment_processing.*;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static Contractor buildContractor() {
        Contractor contractor = new Contractor();
        LegalEntity legalEntity = new LegalEntity();
        contractor.setLegalEntity(legalEntity);
        InternationalLegalEntity internationalLegalEntity = new InternationalLegalEntity();
        legalEntity.setInternationalLegalEntity(internationalLegalEntity);
        internationalLegalEntity
                .setCountry(new CountryRef().setId(CountryCode.findByValue(CountryCode.AUT.getValue())));
        internationalLegalEntity.setLegalName(randomString());
        internationalLegalEntity.setActualAddress(randomString());
        internationalLegalEntity.setRegisteredAddress(randomString());
        return contractor;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static PartyChange createPartyChangeWithPartyModificationAdditionalInfo(String name,
                                                                                   String comment,
                                                                                   String... emails) {
        AdditionalInfoModificationUnit additionalInfoModificationUnit = new AdditionalInfoModificationUnit();
        additionalInfoModificationUnit.setPartyName(name);
        additionalInfoModificationUnit.setComment(comment);
        additionalInfoModificationUnit.setManagerContactEmails(Arrays.stream(emails).toList());
        PartyModification partyModification = new PartyModification();
        partyModification.setAdditionalInfoModification(additionalInfoModificationUnit);
        Claim claim = createClaim();
        claim.setChangeset(List.of(partyModification));
        PartyChange partyChange = new PartyChange();
        partyChange.setClaimCreated(claim);
        return partyChange;
    }

    public static Claim createClaim() {
        Claim claim = new Claim();
        claim.setId(1);
        claim.setCreatedAt("2023-07-03T10:15:30Z");
        claim.setStatus(ClaimStatus.accepted(new ClaimAccepted()));
        claim.setRevision(1);
        return claim;
    }

    public static MachineEvent createPartyEventDataMachineEvent(PartyEventData partyEventData, String id) {
        return new MachineEvent()
                .setEventId(2L)
                .setSourceId(id)
                .setSourceNs("2")
                .setCreatedAt("2021-05-31T06:12:27Z")
                .setData(Value.bin(new ThriftSerializer<>().serialize("", partyEventData)));
    }
}
