package com.empayre.dominator.dao.party.impl;

import com.empayre.dominator.dao.party.iface.PartyDao;
import com.empayre.dominator.domain.tables.pojos.Party;
import com.empayre.dominator.domain.tables.records.PartyRecord;
import com.empayre.dominator.exception.DaoException;
import com.empayre.dominator.exception.NotFoundException;
import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import static com.empayre.dominator.domain.Tables.PARTY;

@Slf4j
@Component
public class PartyDaoImpl extends AbstractGenericDao implements PartyDao {

    private final RowMapper<Party> partyRowMapper;

    public PartyDaoImpl(DataSource dataSource) {
        super(dataSource);
        partyRowMapper = new RecordRowMapper<>(PARTY, Party.class);
    }

    @Override
    public Optional<Long> save(Party party) throws DaoException {
        PartyRecord record = getDslContext().newRecord(PARTY, party);
        Query query = getDslContext()
                .insertInto(PARTY)
                .set(record)
                .onConflict(PARTY.PARTY_ID, PARTY.SEQUENCE_ID, PARTY.CHANGE_ID)
                .doNothing()
                .returning(PARTY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Party get(String partyId) throws DaoException {
        Query query = getDslContext()
                .selectFrom(PARTY)
                .where(PARTY.PARTY_ID.eq(partyId).and(PARTY.CURRENT));

        return Optional.ofNullable(fetchOne(query, partyRowMapper))
                .orElseThrow(() -> new NotFoundException(String.format("Party not found, partyId='%s'", partyId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext()
                .update(PARTY)
                .set(PARTY.CURRENT, false)
                .where(PARTY.ID.eq(id));
        executeOne(query);
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
