package com.empayre.dominator.util;

import dev.vality.dominator.CommonSearchQueryParams;
import dev.vality.dominator.ShopSearchQuery;
import dev.vality.dominator.TerminalSearchQuery;
import dev.vality.dominator.WalletSearchQuery;
import lombok.experimental.UtilityClass;
import org.jooq.Condition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.empayre.dominator.domain.Tables.*;
import static com.empayre.dominator.domain.Tables.WALLET;

@UtilityClass
public class TermSetConditionUtils {

    public static Condition createShopCondition(ShopSearchQuery query) {
        Condition condition = SHOP.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            condition = condition.and(SHOP.ACCOUNT_CURRENCY_CODE.in(commonSearchQueryParams.getCurrencies()));
        }
        if (StringUtils.hasText(query.getPartyId())) {
            condition = condition.and(SHOP.PARTY_ID.eq(query.getPartyId()));
        }
        if (!CollectionUtils.isEmpty(query.getShopIds())) {
            condition = condition.and(SHOP.SHOP_ID.in(query.getShopIds()));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsIds())) {
            List<Integer> termSetIds = query.getTermSetsIds().stream()
                    .map(term -> term.getId())
                    .toList();
            condition = condition.and(CONTRACT.TERMS_ID.in(termSetIds));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsNames())) {
            condition = condition.and(TERM_SET_HIERARCHY.NAME.in(query.getTermSetsNames()));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(SHOP.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }

    public static Condition createWalletCondition(WalletSearchQuery query) {
        Condition condition = WALLET.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            condition = condition.and(WALLET.CURRENCY_CODE.in(commonSearchQueryParams.getCurrencies()));
        }
        if (StringUtils.hasText(query.getPartyId())) {
            condition = condition.and(WALLET.PARTY_ID.eq(query.getPartyId()));
        }
        if (!CollectionUtils.isEmpty(query.getIdentityIds())) {
            List<String> identitiesIds = query.getIdentityIds().stream()
                    .map(identity -> identity.getId())
                    .toList();
            condition = condition.and(WALLET.IDENTITY_ID.in(identitiesIds));
        }
        if (!CollectionUtils.isEmpty(query.getWalletIds())) {
            condition = condition.and(WALLET.WALLET_ID.in(query.getWalletIds()));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsIds())) {
            List<Integer> ids = query.getTermSetsIds().stream()
                    .map(id -> id.getId())
                    .toList();
            condition = condition.and(CONTRACT.TERMS_ID.in(ids));
        }
        if (!CollectionUtils.isEmpty(query.getTermSetsNames())) {
            condition = condition.and(TERM_SET_HIERARCHY.NAME.in(query.getTermSetsNames()));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(WALLET.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }

    public static Condition createTerminalCondition(TerminalSearchQuery query) {
        Condition condition = TERMINAL.CURRENT.isTrue();
        CommonSearchQueryParams commonSearchQueryParams = query.getCommonSearchQueryParams();
        if (!CollectionUtils.isEmpty(commonSearchQueryParams.getCurrencies())) {
            Condition currencyCondition = null;
            for (String currency : commonSearchQueryParams.getCurrencies()) {
                Condition currentCurrencyCondition = PROVIDER.ACCOUNTS_JSON.like("%%s%".formatted(currency));
                currencyCondition = currencyCondition == null
                        ? currentCurrencyCondition : currencyCondition.or(currentCurrencyCondition);

            }
            condition = condition.and(currencyCondition);
        }
        if (!CollectionUtils.isEmpty(query.getTerminalIds())) {
            List<Integer> ids = query.getTerminalIds().stream()
                    .map(refId -> refId.getId())
                    .toList();
            condition = condition.and(TERMINAL.TERMINAL_REF_ID.in(ids));
        }
        if (!CollectionUtils.isEmpty(query.getProviderIds())) {
            List<Integer> ids = query.getProviderIds().stream()
                    .map(refId -> refId.getId())
                    .toList();
            condition = condition.and(PROVIDER.PROVIDER_REF_ID.in(ids));
        }
        String token = commonSearchQueryParams.getContinuationToken();
        if (StringUtils.hasText(token)) {
            condition = condition.and(TERMINAL.ID.lessThan(Long.valueOf(token)));
        }
        return condition;
    }
}
