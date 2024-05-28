package com.empayre.dominator.dao.mapper;

import com.empayre.dominator.data.ShopTermSetDataObject;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static com.empayre.dominator.domain.Tables.*;

public class ShopTermSetDataObjectRowMapper implements RecordMapper<Record, ShopTermSetDataObject> {

    @Override
    public ShopTermSetDataObject map(Record record) {
        return ShopTermSetDataObject.builder()
                .id(record.get(SHOP.ID))
                .partyId(record.get(SHOP.PARTY_ID))
                .shopId(record.get(SHOP.SHOP_ID))
                .contractId(record.get(SHOP.CONTRACT_ID))
                .currency(record.get(SHOP.ACCOUNT_CURRENCY_CODE))
                .shopName(record.get(SHOP.DETAILS_NAME))
                .termSetId(record.get(CONTRACT.TERMS_ID))
                .contractRecordId(record.get(CONTRACT.ID))
                .build();
    }
}
