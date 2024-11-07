package dev.vality.dominator.util;

import dev.vality.dominator.domain.tables.pojos.Shop;
import dev.vality.damsel.domain.ShopAccount;

public class ShopUtil {

    public static void fillShopAccount(Shop shop, ShopAccount shopAccount) {
        shop.setAccountCurrencyCode(shopAccount.getCurrency().getSymbolicCode());
        shop.setAccountGuarantee(shopAccount.getGuarantee());
        shop.setAccountSettlement(shopAccount.getSettlement());
        shop.setAccountPayout(shopAccount.getPayout());
    }
}
