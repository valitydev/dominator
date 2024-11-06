package dev.vality.dominator.dao.party.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.mapper.ShopTermSetDataObjectRowMapper;
import dev.vality.dominator.dao.mapper.TerminalTermSetDataObjectRowMapper;
import dev.vality.dominator.dao.mapper.WalletTermSetDataObjectRowMapper;
import dev.vality.dominator.dao.party.iface.TermSetDao;
import dev.vality.dominator.data.ShopTermSetDataObject;
import dev.vality.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.data.WalletTermSetDataObject;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static dev.vality.dominator.domain.Tables.*;

@Component
public class TermSetDaoImpl extends AbstractDao implements TermSetDao {

    private final RecordMapper<Record, ShopTermSetDataObject> shopTermSetDataObjectRowMapper;
    private final RecordMapper<Record, WalletTermSetDataObject> walletTermSetDataObjectRowMapper;
    private final RecordMapper<Record, TerminalTermSetDataObject> terminalTermSetDataObjectRowMapper;

    @Value("${service.default.limit}")
    private int defaultLimit;

    public TermSetDaoImpl(DataSource dataSource) {
        super(dataSource);
        shopTermSetDataObjectRowMapper = new ShopTermSetDataObjectRowMapper();
        walletTermSetDataObjectRowMapper = new WalletTermSetDataObjectRowMapper();
        terminalTermSetDataObjectRowMapper = new TerminalTermSetDataObjectRowMapper();
    }

    @Override
    public List<ShopTermSetDataObject> getShopTermSets(Condition condition, int limit) {
        var fetch = getDslContext()
                .select(SHOP.ID, SHOP.PARTY_ID, SHOP.SHOP_ID, SHOP.CONTRACT_ID, SHOP.ACCOUNT_CURRENCY_CODE,
                        SHOP.DETAILS_NAME, CONTRACT.TERMS_ID, CONTRACT.ID)
                .from(SHOP)
                .join(CONTRACT)
                .on(CONTRACT.CONTRACT_ID.eq(SHOP.CONTRACT_ID).and(CONTRACT.CURRENT))
                .join(TERM_SET_HIERARCHY)
                .on(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(CONTRACT.TERMS_ID).and(TERM_SET_HIERARCHY.CURRENT))
                .where(condition)
                .orderBy(SHOP.ID.desc())
                .limit(getLimit(limit))
                .fetch();
        return fetch
                .map(shopTermSetDataObjectRowMapper);
    }

    @Override
    public List<WalletTermSetDataObject> getWalletTermSets(Condition condition, int limit) {
        var fetch = getDslContext()
                .select(WALLET.PARTY_ID, WALLET.IDENTITY_ID, IDENTITY.PARTY_CONTRACT_ID, WALLET.CURRENCY_CODE,
                        WALLET.WALLET_ID, WALLET.WALLET_NAME, CONTRACT.TERMS_ID, CONTRACT.ID, WALLET.ID)
                .from(WALLET)
                .join(IDENTITY)
                .on(WALLET.IDENTITY_ID.eq(IDENTITY.IDENTITY_ID).and(IDENTITY.CURRENT))
                .join(CONTRACT)
                .on(CONTRACT.CONTRACT_ID.eq(IDENTITY.PARTY_CONTRACT_ID).and(CONTRACT.CURRENT))
                .join(TERM_SET_HIERARCHY)
                .on(TERM_SET_HIERARCHY.TERM_SET_HIERARCHY_REF_ID.eq(CONTRACT.TERMS_ID).and(TERM_SET_HIERARCHY.CURRENT))
                .where(condition)
                .orderBy(WALLET.ID.desc())
                .limit(getLimit(limit))
                .fetch();
        return fetch
                .map(walletTermSetDataObjectRowMapper);
    }

    @Override
    public List<TerminalTermSetDataObject> getTerminalTermSets(Condition condition, int limit) {
        var fetch = getDslContext()
                .select(TERMINAL.TERMINAL_REF_ID, TERMINAL.NAME, PROVIDER.PROVIDER_REF_ID, PROVIDER.NAME,
                        TERMINAL.TERMS_JSON, PROVIDER.PAYMENT_TERMS_JSON, PROVIDER.WALLET_TERMS_JSON,
                        PROVIDER.ACCOUNTS_JSON, TERMINAL.ID, TERMINAL.TERMS_OBJECT)
                .from(TERMINAL)
                .join(PROVIDER)
                .on(TERMINAL.TERMINAL_PROVIDER_REF_ID.eq(PROVIDER.PROVIDER_REF_ID).and(PROVIDER.CURRENT))
                .where(condition)
                .orderBy(TERMINAL.ID.desc())
                .limit(getLimit(limit))
                .fetch();
        return fetch
                .map(terminalTermSetDataObjectRowMapper);
    }

    private int getLimit(int sourceLimit) {
        return sourceLimit < 1 ? defaultLimit : sourceLimit;
    }
}
