package dev.vality.dominator.dao.mapper;

import dev.vality.dominator.data.TerminalTermSetDataObject;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static dev.vality.dominator.domain.Tables.PROVIDER;
import static dev.vality.dominator.domain.Tables.TERMINAL;

public class TerminalTermSetDataObjectRowMapper implements RecordMapper<Record, TerminalTermSetDataObject> {

    @Override
    public TerminalTermSetDataObject map(Record record) {
        return TerminalTermSetDataObject.builder()
                .id(record.get(TERMINAL.ID))
                .terminalId(record.get(TERMINAL.TERMINAL_REF_ID))
                .terminalName(record.get(TERMINAL.NAME))
                .providerId(record.get(PROVIDER.PROVIDER_REF_ID))
                .providerName(record.get(PROVIDER.NAME))
                .termSetJson(record.get(TERMINAL.TERMS_JSON))
                .termSetObject(record.get(TERMINAL.TERMS_OBJECT))
                .paymentTermSetJson(record.get(PROVIDER.PAYMENT_TERMS_JSON))
                .walletTermSetJson(record.get(PROVIDER.WALLET_TERMS_JSON))
                .accountsJson(record.get(PROVIDER.ACCOUNTS_JSON))
                .build();
    }
}
