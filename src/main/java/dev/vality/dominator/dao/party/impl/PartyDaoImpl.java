package dev.vality.dominator.dao.party.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.party.iface.PartyDao;
import dev.vality.dominator.domain.tables.pojos.Party;
import dev.vality.dominator.domain.tables.records.PartyRecord;
import dev.vality.dominator.exception.DaoException;
import dev.vality.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.dominator.domain.Tables.PARTY;

@Slf4j
@Component
public class PartyDaoImpl extends AbstractDao implements PartyDao {

    public PartyDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Long> save(Party party) throws DaoException {
        PartyRecord record = getDslContext().newRecord(PARTY, party);
        Long id = getDslContext()
                .insertInto(PARTY)
                .set(record)
                .onConflict(PARTY.PARTY_ID, PARTY.SEQUENCE_ID, PARTY.CHANGE_ID)
                .doNothing()
                .returning(PARTY.ID)
                .fetchOne()
                .getId();

        return Optional.ofNullable(id).map(Number::longValue);
    }

    @NotNull
    @Override
    public Party get(String partyId) throws DaoException {
        Party party = getDslContext()
                .selectFrom(PARTY)
                .where(PARTY.PARTY_ID.eq(partyId).and(PARTY.CURRENT))
                .fetchOneInto(Party.class);

        return Optional.ofNullable(party)
                .orElseThrow(() -> new NotFoundException(String.format("Party not found, partyId='%s'", partyId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        getDslContext()
                .update(PARTY)
                .set(PARTY.CURRENT, false)
                .where(PARTY.ID.eq(id))
                .execute();
    }

    @Override
    public void saveWithUpdateCurrent(Party partySource, Long oldId, String eventName) {
        save(partySource)
                .ifPresentOrElse(
                        saveResult -> {
                            updateNotCurrent(oldId);
                            log.info("Party {} has been saved, sequenceId={}, partyId={}, changeId={}", eventName,
                                    partySource.getSequenceId(), partySource.getPartyId(), partySource.getChangeId());
                        },
                        () -> log.info("Party {} duplicated, sequenceId={}, partyId={}, changeId={}", eventName,
                                partySource.getSequenceId(), partySource.getPartyId(), partySource.getChangeId())
                );
    }
}
