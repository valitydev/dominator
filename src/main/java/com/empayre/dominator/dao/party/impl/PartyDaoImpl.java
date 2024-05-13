package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.AbstractDao;
import com.empayre.dominator.dao.party.iface.PartyDao;
import com.empayre.dominator.domain.tables.pojos.Party;
import com.empayre.dominator.domain.tables.records.PartyRecord;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static com.empayre.dominator.domain.Tables.PARTY;

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
                .fetchOneInto(Long.class);

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
