package com.empayre.dominator.service;

import com.empayre.dominator.config.KafkaPostgresqlSpringBootITest;
import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.dao.identity.iface.IdentityDao;
import com.empayre.dominator.dao.party.iface.ContractDao;
import com.empayre.dominator.dao.party.iface.ShopDao;
import com.empayre.dominator.dao.party.iface.TermSetHierarchyDao;
import com.empayre.dominator.dao.wallet.iface.WalletDao;
import com.empayre.dominator.domain.tables.pojos.Provider;
import com.empayre.dominator.domain.tables.pojos.Terminal;
import dev.vality.damsel.domain.IdentityProviderRef;
import dev.vality.damsel.domain.ProviderRef;
import dev.vality.damsel.domain.TermSetHierarchyRef;
import dev.vality.damsel.domain.TerminalRef;
import dev.vality.dominator.*;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.empayre.dominator.generator.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@KafkaPostgresqlSpringBootITest
public class DominatorServiceTest {

    @Autowired
    private DominatorService dominatorService;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private IdentityDao identityDao;

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private DomainObjectDao<Terminal, Integer> terminalDao;

    @Autowired
    private DomainObjectDao<Provider, Integer> providerDao;

    @Autowired
    private TermSetHierarchyDao termSetHierarchyDao;

    @Test
    void searchShopTermSetsTest() throws TException {
        for (int i = 1; i <= 10; i++) {
            String partyId = "party-" + i;
            String shopId = partyId + "-shop-" + i;
            String contractId = "Contract-" + i;
            Integer termsId = i;
            for (int j = 1; j <= 3; j++) {
                shopDao.save(generateNewShop(partyId, shopId, contractId, false));
                contractDao.save(generateNewContract(partyId, contractId, termsId, false));
                termSetHierarchyDao.save(generateNewTermSetHierarchy(Long.valueOf(i), termsId, false));
            }
            shopDao.save(generateNewShop(partyId, shopId, contractId, true));
            contractDao.save(generateNewContract(partyId, contractId, termsId, true));
            termSetHierarchyDao.save(generateNewTermSetHierarchy(Long.valueOf(i), termsId, true));
        }

        ShopTermSetsResponse simpleResponse = dominatorService.searchShopTermSets(
                new ShopSearchQuery().setCommonSearchQueryParams(new CommonSearchQueryParams()));
        assertNotNull(simpleResponse);
        assertNotNull(simpleResponse.getContinuationToken());
        assertNotNull(simpleResponse.getTerms());
        assertEquals(10, simpleResponse.getTerms().size());

        ShopTermSetsResponse partyResponse = dominatorService.searchShopTermSets(
                new ShopSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setPartyId("party-1")
        );
        assertNotNull(partyResponse);
        assertNotNull(partyResponse.getTerms());
        assertEquals(1, partyResponse.getTerms().size());

        ShopTermSetsResponse shopResponse = dominatorService.searchShopTermSets(
                new ShopSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setShopIds(List.of("party-1-shop-1", "party-2-shop-2", "party-2-shop-3", "party-3-shop-3"))
        );
        assertNotNull(shopResponse);
        assertEquals(3, shopResponse.getTerms().size());

        ShopTermSetsResponse termSetsIdsResponse = dominatorService.searchShopTermSets(
                new ShopSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setTermSetsIds(List.of(
                                new TermSetHierarchyRef(1),
                                new TermSetHierarchyRef(3),
                                new TermSetHierarchyRef(5)))
        );
        assertNotNull(termSetsIdsResponse);
        assertEquals(3, termSetsIdsResponse.getTerms().size());

        ShopTermSetsResponse termSetsNamesResponse = dominatorService.searchShopTermSets(
                new ShopSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setTermSetsNames(List.of("Name-1", "Name-6"))
        );
        assertNotNull(termSetsNamesResponse);
        assertEquals(2, termSetsNamesResponse.getTerms().size());
    }

    @Test
    void searchWalletTermSetsTest() throws TException {
        for (int i = 1; i <= 10; i++) {
            String identityId = "Identity-" + i;
            String walletId = "Wallet-" + i;
            String partyId = "WParty-" + i;
            String contractId = "WContract-" + i;
            Integer termsId = i;
            for (int j = 1; j <= 3; j++) {
                walletDao.save(generateNewWallet(walletId, identityId, partyId, false));
                identityDao.save(generateNewIdentity(identityId, partyId, contractId, false));
                contractDao.save(generateNewContract(partyId, contractId, termsId, false));
                termSetHierarchyDao.save(generateNewTermSetHierarchy(Long.valueOf(i), termsId, false));
            }
            walletDao.save(generateNewWallet(walletId, identityId, partyId, true));
            identityDao.save(generateNewIdentity(identityId, partyId, contractId, true));
            contractDao.save(generateNewContract(partyId, contractId, termsId, true));
            termSetHierarchyDao.save(generateNewTermSetHierarchy(Long.valueOf(i), termsId, true));
        }

        WalletTermSetsResponse emptyRequestResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams())
        );
        assertNotNull(emptyRequestResponse);
        assertEquals(10, emptyRequestResponse.getTerms().size());

        WalletTermSetsResponse partyResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setPartyId("WParty-1")
        );
        assertNotNull(partyResponse);
        assertEquals(1, partyResponse.getTerms().size());

        WalletTermSetsResponse walletResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setWalletIds(List.of("Wallet-9", "Wallet-8", "Wallet-81"))
        );
        assertNotNull(walletResponse);
        assertEquals(2, walletResponse.getTerms().size());

        WalletTermSetsResponse identityResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setIdentityIds(List.of(
                                new IdentityProviderRef("Identity-3"),
                                new IdentityProviderRef("Identity-5"),
                                new IdentityProviderRef("Identity-7")
                        ))
        );
        assertNotNull(identityResponse);
        assertEquals(3, identityResponse.getTerms().size());

        WalletTermSetsResponse termIdsResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setTermSetsIds(List.of(
                                new TermSetHierarchyRef(3),
                                new TermSetHierarchyRef(5),
                                new TermSetHierarchyRef(6),
                                new TermSetHierarchyRef(7)
                        ))
        );
        assertNotNull(termIdsResponse);
        assertEquals(4, termIdsResponse.getTerms().size());

        WalletTermSetsResponse termNamesResponse = dominatorService.searchWalletTermSets(
                new WalletSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(10))
                        .setTermSetsNames(List.of("Name-1", "Name-6"))
        );
        assertNotNull(termNamesResponse);
        assertEquals(2, termNamesResponse.getTerms().size());
    }

    @Test
    void searchTerminalTermSetsTest() throws TException {
        for (int i = 1; i <= 10; i++) {
            Integer terminalId = i;
            Integer providerId = i;
            Long version = Long.valueOf(i);
            for (int j = 1; j <= 3; j++) {
                terminalDao.save(generateNewTerminal(version, terminalId, providerId, false));
                providerDao.save(generateNewPaymentProvider(version, providerId, false));
            }
            terminalDao.save(generateNewTerminal(version, terminalId, providerId, true));
            providerDao.save(generateNewPaymentProvider(version, providerId, true));
        }

        TerminalTermSetsResponse simpleResponse = dominatorService.searchTerminalTermSets(
                new TerminalSearchQuery().setCommonSearchQueryParams(new CommonSearchQueryParams()));
        assertNotNull(simpleResponse);
        assertEquals(10, simpleResponse.getTerms().size());


        TerminalTermSetsResponse terminalIdsResponse = dominatorService.searchTerminalTermSets(
                new TerminalSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(20))
                        .setTerminalIds(List.of(
                                new TerminalRef(2),
                                new TerminalRef(4),
                                new TerminalRef(7)
                        ))
        );
        assertNotNull(terminalIdsResponse);
        assertEquals(3, terminalIdsResponse.getTerms().size());

        TerminalTermSetsResponse providerIdsResponse = dominatorService.searchTerminalTermSets(
                new TerminalSearchQuery()
                        .setCommonSearchQueryParams(new CommonSearchQueryParams().setLimit(20))
                        .setProviderIds(List.of(
                                new ProviderRef(1),
                                new ProviderRef(2),
                                new ProviderRef(4),
                                new ProviderRef(9),
                                new ProviderRef(7)
                        ))
        );
        assertNotNull(providerIdsResponse);
        assertEquals(5, providerIdsResponse.getTerms().size());
    }
}
