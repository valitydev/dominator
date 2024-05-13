package com.empayre.dominator.dao.mapper;

import com.empayre.dominator.data.WalletTermSetDataObject;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static com.empayre.dominator.domain.Tables.*;

public class WalletTermSetDataObjectRowMapper implements RecordMapper<Record, WalletTermSetDataObject> {

    @Override
    public WalletTermSetDataObject map(Record record) {
        return WalletTermSetDataObject.builder()
                .id(record.get(WALLET.ID))
                .partyId(record.get(WALLET.PARTY_ID))
                .identityId(record.get(WALLET.IDENTITY_ID))
                .contractId(record.get(IDENTITY.PARTY_CONTRACT_ID))
                .currency(record.get(WALLET.CURRENCY_CODE))
                .walletId(record.get(WALLET.WALLET_ID))
                .walletName(record.get(WALLET.WALLET_NAME))
                .termSetId(record.get(CONTRACT.TERMS_ID))
                .termSetName(record.get(TERM_SET_HIERARCHY.NAME))
                .currentTermSetJson(record.get(TERM_SET_HIERARCHY.TERM_SETS_JSON))
                .currentTermSetHierarchyObject(record.get(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_OBJECT))
                .build();
    }
}
