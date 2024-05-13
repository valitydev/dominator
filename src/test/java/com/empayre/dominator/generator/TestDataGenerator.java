package com.empayre.dominator.generator;

import com.empayre.dominator.domain.enums.Blocking;
import com.empayre.dominator.domain.enums.ContractStatus;
import com.empayre.dominator.domain.enums.Suspension;
import com.empayre.dominator.domain.tables.pojos.*;
import com.empayre.dominator.domain.tables.pojos.Contract;
import com.empayre.dominator.domain.tables.pojos.Provider;
import com.empayre.dominator.domain.tables.pojos.Shop;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.domain.tables.pojos.Terminal;
import com.empayre.dominator.domain.tables.pojos.Wallet;
import dev.vality.damsel.base.BoundType;
import dev.vality.damsel.base.Rational;
import dev.vality.damsel.base.TimestampInterval;
import dev.vality.damsel.base.TimestampIntervalBound;
import dev.vality.damsel.domain.*;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TestDataGenerator {

    public static Shop generateNewShop(String partyId, String shopId, String contractId, boolean current) {
        Shop shop = new Shop();
        shop.setCreatedAt(LocalDateTime.now());
        shop.setEventCreatedAt(LocalDateTime.now());
        shop.setPartyId(partyId);
        shop.setShopId(shopId);
        shop.setAccountCurrencyCode("RUB");
        shop.setBlocking(Blocking.unblocked);
        shop.setSuspension(Suspension.active);
        shop.setDetailsName("name");
        shop.setLocationUrl("url");
        shop.setCategoryId(1);
        shop.setContractId(contractId);
        shop.setCurrent(current);
        return shop;
    }

    public static Contract generateNewContract(String partyId, String contractId, Integer termsId, boolean current) {
        Contract contract = new Contract();
        contract.setCreatedAt(LocalDateTime.now());
        contract.setEventCreatedAt(LocalDateTime.now());
        contract.setContractId(contractId);
        contract.setPartyId(partyId);
        contract.setPaymentInstitutionId(1);
        contract.setStatus(ContractStatus.active);
        contract.setTermsId(termsId);
        contract.setCurrent(current);
        return contract;
    }

    public static TermSetHierarchy generateNewTermSetHierarchy(Long versionId,
                                                               Integer termsId,
                                                               boolean current) throws TException {
        TermSetHierarchy termSetHierarchy = new TermSetHierarchy();
        termSetHierarchy.setVersionId(versionId);
        termSetHierarchy.setTermSetHierarchyRefId(termsId);
        termSetHierarchy.setName("Name-" + termsId);
        termSetHierarchy.setTermSetsJson("[{\"action_time\":{\"lower_bound\":{\"bound_type\":\"inclusive\"," +
                "\"bound_time\":\"2017-10-01T00:00:00Z\"}},\"terms\":{\"payments\":{\"fees\":{\"value\":[\"list\"," +
                "{\"source\":{\"merchant\":\"settlement\"},\"destination\":{\"system\":\"settlement\"}," +
                "\"volume\":{\"product\":{\"max_of\":[\"set\",{\"share\":{\"parts\":{\"p\":11,\"q\":100}," +
                "\"of\":\"operation_amount\"}},{\"fixed\":{\"cash\":{\"amount\":150," +
                "\"currency\":{\"symbolic_code\":\"EUR\"}}}}]}}},{\"source\":{\"merchant\":\"settlement\"}," +
                "\"destination\":{\"system\":\"settlement\"},\"volume\":{\"fixed\":{\"cash\":{\"amount\":30," +
                "\"currency\":{\"symbolic_code\":\"EUR\"}}}}}]}}}}]\n");
        termSetHierarchy.setTermSetHierarchyObject(new TSerializer().serialize(createNewTermSetHierarchyObject()));
        termSetHierarchy.setCurrent(current);
        return termSetHierarchy;
    }

    public static Wallet generateNewWallet(String walletId, String identityId, String partyId, boolean current) {
        Wallet wallet = new Wallet();
        wallet.setEventCreatedAt(LocalDateTime.now());
        wallet.setEventOccuredAt(LocalDateTime.now());
        wallet.setSequenceId(1);
        wallet.setWalletId(walletId);
        wallet.setWalletName("Wallet-" + walletId);
        wallet.setIdentityId(identityId);
        wallet.setPartyId(partyId);
        wallet.setCurrencyCode("RUB");
        wallet.setCurrent(current);
        return wallet;
    }

    public static Identity generateNewIdentity(String identityId, String partyId, String contractId, boolean current) {
        Identity identity = new Identity();
        identity.setEventCreatedAt(LocalDateTime.now());
        identity.setEventOccuredAt(LocalDateTime.now());
        identity.setSequenceId(1);
        identity.setPartyId(partyId);
        identity.setPartyContractId(contractId);
        identity.setIdentityId(identityId);
        identity.setIdentityProviderId("provId");
        identity.setCurrent(current);
        return identity;
    }

    public static Terminal generateNewTerminal(Long versionId,
                                               Integer terminalRefId,
                                               Integer terminalProviderRefId,
                                               boolean current)
            throws TException {
        Terminal terminal = new Terminal();
        terminal.setVersionId(versionId);
        terminal.setTerminalRefId(terminalRefId);
        terminal.setName("TermRef-" + terminalRefId);
        terminal.setDescription("Desc");
        terminal.setTermsJson("");
        terminal.setTermsObject(new TSerializer().serialize(generateNewProvisionTermSet()));
        terminal.setCurrent(current);
        terminal.setTerminalProviderRefId(terminalProviderRefId);
        return terminal;
    }

    public static Provider generateNewPaymentProvider(Long versionId, Integer providerRefId, boolean current)
            throws TException {
        Provider provider = new Provider();
        provider.setVersionId(versionId);
        provider.setProviderRefId(providerRefId);
        provider.setName("Provider-" + provider);
        provider.setDescription("Desc");
        provider.setProxyRefId(1);
        provider.setPaymentTermsJson("{\"currencies\":{\"value\":[\"set\",{\"symbolic_code\":\"UZS\"}," +
                "{\"symbolic_code\":\"RUB\"}]},\"categories\":{\"value\":[\"set\",{\"id\":8999}]}," +
                "\"payment_methods\":{\"value\":[\"set\",{\"id\":{\"payment_terminal\":{\"id\":\"P2P H2H\"}}}]}," +
                "\"cash_limit\":{\"decisions\":[\"list\",{\"if_\":{\"condition\":{\"currency_is\":" +
                "{\"symbolic_code\":\"RUB\"}}},\"then_\":{\"value\":{\"upper\":{\"inclusive\":" +
                "{\"amount\":100000000,\"currency\":{\"symbolic_code\":\"RUB\"}}},\"lower\":{\"inclusive\":" +
                "{\"amount\":100,\"currency\":{\"symbolic_code\":\"RUB\"}}}}}},{\"if_\":{\"condition\":" +
                "{\"currency_is\":{\"symbolic_code\":\"UZS\"}}},\"then_\":{\"value\":{\"upper\":{\"inclusive\":" +
                "{\"amount\":100000000,\"currency\":{\"symbolic_code\":\"UZS\"}}},\"lower\":{\"inclusive\":" +
                "{\"amount\":100,\"currency\":{\"symbolic_code\":\"UZS\"}}}}}}]},\"cash_flow\":{\"value\":" +
                "[\"list\",{\"source\":{\"provider\":\"settlement\"},\"destination\":{\"merchant\":\"settlement\"}," +
                "\"volume\":{\"share\":{\"parts\":{\"p\":1,\"q\":1},\"of\":\"operation_amount\"}}}," +
                "{\"source\":{\"system\":\"settlement\"},\"destination\":{\"provider\":\"settlement\"}," +
                "\"volume\":{\"share\":{\"parts\":{\"p\":1,\"q\":100},\"of\":\"operation_amount\"}}}]}," +
                "\"refunds\":{\"cash_flow\":{\"value\":[\"list\",{\"source\":{\"merchant\":\"settlement\"}," +
                "\"destination\":{\"provider\":\"settlement\"},\"volume\":{\"share\":{\"parts\":{\"p\":1,\"q\":1}," +
                "\"of\":\"operation_amount\"}}}]},\"partial_refunds\":{\"cash_limit\":{\"decisions\":[\"list\"," +
                "{\"if_\":{\"condition\":{\"currency_is\":{\"symbolic_code\":\"RUB\"}}},\"then_\":{\"value\":" +
                "{\"upper\":{\"inclusive\":{\"amount\":100000000,\"currency\":{\"symbolic_code\":\"RUB\"}}}," +
                "\"lower\":{\"inclusive\":{\"amount\":100,\"currency\":{\"symbolic_code\":\"RUB\"}}}}}}," +
                "{\"if_\":{\"condition\":{\"currency_is\":{\"symbolic_code\":\"UZS\"}}}," +
                "\"then_\":{\"value\":{\"upper\":{\"inclusive\":{\"amount\":100000000,\"currency\":" +
                "{\"symbolic_code\":\"UZS\"}}},\"lower\":{\"inclusive\":{\"amount\":100,\"currency\":" +
                "{\"symbolic_code\":\"UZS\"}}}}}}]}}},\"chargebacks\":{\"cash_flow\":{\"value\":[\"list\"," +
                "{\"source\":{\"system\":\"settlement\"},\"destination\":{\"provider\":\"settlement\"},\"volume\":" +
                "{\"share\":{\"parts\":{\"p\":1,\"q\":1},\"of\":\"operation_amount\"}}}]}}}\n");
        provider.setPaymentTermsObject(new TSerializer().serialize(generateNewPaymentsProvisionTerms()));
        provider.setAccountsJson("{\"RUB\":17472920,\"UZS\":12791085}\n");
        provider.setCurrent(current);
        return provider;
    }

    public static Provider generateNewWalletProvider(Long versionId, Integer providerRefId, boolean current)
            throws TException {
        Provider provider = new Provider();
        provider.setVersionId(versionId);
        provider.setProviderRefId(providerRefId);
        provider.setName("Provider-" + provider);
        provider.setDescription("Desc");
        provider.setProxyRefId(1);
        provider.setWalletTermsJson("{\"withdrawals\":{\"currencies\":{\"value\":[\"set\",{\"symbolic_code\":\"KZT\"}" +
                ",{\"symbolic_code\":\"RUB\"}]},\"payout_methods\":{\"value\":[\"set\",{\"id\":\"wallet_info\"}]}," +
                "\"cash_limit\":{\"decisions\":[\"list\",{\"if_\":{\"condition\":{\"currency_is\":" +
                "{\"symbolic_code\":\"RUB\"}}},\"then_\":{\"value\":{\"upper\":{\"inclusive\":{\"amount\":100000000," +
                "\"currency\":{\"symbolic_code\":\"RUB\"}}},\"lower\":{\"inclusive\":{\"amount\":100,\"currency\":" +
                "{\"symbolic_code\":\"RUB\"}}}}}},{\"if_\":{\"condition\":{\"currency_is\":{\"symbolic_code\":" +
                "\"KZT\"}}},\"then_\":{\"value\":{\"upper\":{\"inclusive\":{\"amount\":1000000000,\"currency\":" +
                "{\"symbolic_code\":\"KZT\"}}},\"lower\":{\"inclusive\":{\"amount\":100,\"currency\":" +
                "{\"symbolic_code\":\"KZT\"}}}}}}]},\"cash_flow\":{\"value\":[\"list\",{\"source\":{\"system\"" +
                ":\"settlement\"},\"destination\":{\"provider\":\"settlement\"},\"volume\":{\"share\":{\"parts\":" +
                "{\"p\":1,\"q\":100},\"of\":\"operation_amount\"}}}]}}}\n");
        provider.setWalletTermsObject(new TSerializer().serialize(createNewWalletProvisionTerms()));
        provider.setAccountsJson("{\"RUB\":17472920,\"UZS\":12791085}\n");
        provider.setCurrent(current);
        return provider;
    }

    public static WalletProvisionTerms createNewWalletProvisionTerms() {
        return new WalletProvisionTerms()
                .setWithdrawals(
                        new WithdrawalProvisionTerms()
                                .setCurrencies(CurrencySelector.value(Set.of(new CurrencyRef().setSymbolicCode("RUB"))))
                                .setPayoutMethods(PayoutMethodSelector.value(Set.of(
                                        new PayoutMethodRef()
                                                .setId(PayoutMethod.wallet_info)
                                )))
                                .setCashFlow(CashFlowSelector.value(createCashFlowPostings()))
                );
    }

    public static ProvisionTermSet generateNewProvisionTermSet() {
        return new ProvisionTermSet()
                .setPayments(
                        new PaymentsProvisionTerms()
                                .setCashLimit(CashLimitSelector.value(
                                        new CashRange()
                                                .setUpper(generateNewCashBound(5000L))
                                                .setLower(generateNewCashBound(500L))
                                ))
                                .setCashFlow(CashFlowSelector.value(createCashFlowPostings()))
                );
    }

    public static CashBound generateNewCashBound(long amount) {
        return CashBound.inclusive(
                new Cash()
                        .setAmount(amount)
                        .setCurrency(new CurrencyRef().setSymbolicCode("RUB")));
    }

    public static PaymentsProvisionTerms generateNewPaymentsProvisionTerms() {
        return new PaymentsProvisionTerms()
                .setCurrencies(CurrencySelector.value(Set.of(new CurrencyRef().setSymbolicCode("RUB"))))
                .setCategories(CategorySelector.value(Set.of(new CategoryRef().setId(9999))))
                .setPaymentMethods(PaymentMethodSelector.value(Set.of(
                        new PaymentMethodRef()
                                .setId(PaymentMethod.payment_terminal(new PaymentServiceRef().setId("id123")))
                )))
                .setCashLimit(CashLimitSelector.value(new CashRange()
                        .setUpper(generateNewCashBound(50000L))
                        .setLower(generateNewCashBound(5000L))))
                .setCashFlow(CashFlowSelector.value(createCashFlowPostings()));
    }

    public static TermSetHierarchyObject createNewTermSetHierarchyObject() {
        CashFlowSelector cashFlowSelector = new CashFlowSelector();
        cashFlowSelector.setValue(createCashFlowPostings());
        TimedTermSet termSet = new TimedTermSet()
                .setTerms(new TermSet().setPayments(new PaymentsServiceTerms().setFees(cashFlowSelector)))
                .setActionTime(
                        new TimestampInterval()
                                .setLowerBound(
                                        new TimestampIntervalBound()
                                                .setBoundType(BoundType.inclusive)
                                                .setBoundTime("2017-10-01T00:00:00Z")
                                )
                );
        return new TermSetHierarchyObject()
                .setRef(new TermSetHierarchyRef().setId(123))
                .setData(
                        new dev.vality.damsel.domain.TermSetHierarchy()
                                .setName("TermName")
                                .setDescription("TermDesc")
                                .setTermSets(List.of(termSet))
                );
    }

    public static List<CashFlowPosting> createCashFlowPostings() {
        CashFlowPosting posting = new CashFlowPosting();
        CashFlowAccount sourceCashFlowAccount = new CashFlowAccount();
        sourceCashFlowAccount.setMerchant(MerchantCashFlowAccount.settlement);
        posting.setSource(sourceCashFlowAccount);

        CashFlowAccount destinationCashFlowAccount = new CashFlowAccount();
        destinationCashFlowAccount.setSystem(SystemCashFlowAccount.settlement);
        posting.setDestination(destinationCashFlowAccount);

        CashVolumeProduct cashVolumeProduct = new CashVolumeProduct();
        CashVolume fixed = CashVolume.fixed(
                new CashVolumeFixed().setCash(new Cash()
                        .setAmount(150L)
                        .setCurrency(new CurrencyRef().setSymbolicCode("EUR")))
        );
        CashVolume share = CashVolume.share(
                new CashVolumeShare()
                        .setOf(CashFlowConstant.operation_amount)
                        .setParts(new Rational().setP(11L).setQ(100L))
        );
        cashVolumeProduct.setMaxOf(Set.of(share, fixed));
        CashVolume cashVolume = new CashVolume();
        cashVolume.setProduct(cashVolumeProduct);
        posting.setVolume(cashVolume);
        return List.of(posting);
    }
}
