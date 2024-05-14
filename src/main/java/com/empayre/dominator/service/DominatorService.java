package com.empayre.dominator.service;

import com.empayre.dominator.handler.get.GetTermSetsHandler;
import dev.vality.damsel.base.InvalidRequest;
import dev.vality.dominator.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DominatorService implements DominatorServiceSrv.Iface {

    private final GetTermSetsHandler<ShopSearchQuery, ShopTermSetsResponse> getShopTermSetsHandler;
    private final GetTermSetsHandler<WalletSearchQuery, WalletTermSetsResponse> getWalletTermSetsHandler;
    private final GetTermSetsHandler<TerminalSearchQuery, TerminalTermSetsResponse> getTerminalTermSetsHandler;

    @Override
    public ShopTermSetsResponse searchShopTermSets(ShopSearchQuery shopSearchQuery)
            throws BadContinuationToken, LimitExceeded, InvalidRequest, TException {
        return getShopTermSetsHandler.handle(shopSearchQuery);
    }

    @Override
    public WalletTermSetsResponse searchWalletTermSets(WalletSearchQuery walletSearchQuery)
            throws BadContinuationToken, LimitExceeded, InvalidRequest, TException {
        return getWalletTermSetsHandler.handle(walletSearchQuery);
    }

    @Override
    public TerminalTermSetsResponse searchTerminalTermSets(TerminalSearchQuery terminalSearchQuery)
            throws BadContinuationToken, LimitExceeded, InvalidRequest, TException {
        return getTerminalTermSetsHandler.handle(terminalSearchQuery);
    }
}
