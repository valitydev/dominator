package com.empayre.dominator.handler.dominant.impl;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.dao.dominant.impl.ProviderDaoImpl;
import com.empayre.dominator.domain.tables.pojos.Provider;
import com.empayre.dominator.handler.dominant.AbstractDominantHandler;
import com.empayre.dominator.util.JsonUtil;
import dev.vality.damsel.domain.ProviderObject;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProviderHandler extends AbstractDominantHandler<ProviderObject, Provider, Integer> {

    private final ProviderDaoImpl providerDao;

    public ProviderHandler(ProviderDaoImpl providerDao) {
        this.providerDao = providerDao;
    }

    @Override
    protected DomainObjectDao<Provider, Integer> getDomainObjectDao() {
        return providerDao;
    }

    @Override
    protected ProviderObject getTargetObject() {
        return getDomainObject().getProvider();
    }

    @Override
    protected Integer getTargetObjectRefId() {
        return getTargetObject().getRef().getId();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetProvider();
    }

    @Override
    public Provider convertToDatabaseObject(ProviderObject providerObject, Long versionId, boolean current) {
        Provider provider = new Provider();
        provider.setVersionId(versionId);
        provider.setProviderRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.Provider data = providerObject.getData();
        provider.setName(data.getName());
        provider.setDescription(data.getDescription());
        provider.setProxyRefId(data.getProxy().getRef().getId());
        if (data.isSetAbsAccount()) {
            provider.setAbsAccount(data.getAbsAccount());
        }

        if (data.isSetTerms() && data.getTerms().isSetPayments()) {
            provider.setPaymentTermsJson(JsonUtil.thriftBaseToJsonString(data.getTerms().getPayments()));
        } else if (data.isSetPaymentTerms()) {
            provider.setPaymentTermsJson(JsonUtil.thriftBaseToJsonString(data.getPaymentTerms()));
        }

        if (data.isSetTerms() && data.getTerms().isSetRecurrentPaytools()) {
            provider.setRecurrentPaytoolTermsJson(
                    JsonUtil.thriftBaseToJsonString(data.getTerms().getRecurrentPaytools()));
        } else if (data.isSetRecurrentPaytoolTerms()) {
            provider.setRecurrentPaytoolTermsJson(JsonUtil.thriftBaseToJsonString(data.getRecurrentPaytoolTerms()));
        }

        if (data.isSetIdentity()) {
            provider.setIdentity(data.getIdentity());
        }
        if (data.isSetTerms() && data.getTerms().isSetWallet()) {
            provider.setWalletTermsJson(JsonUtil.thriftBaseToJsonString(data.getTerms().getWallet()));
        }
        if (data.isSetParamsSchema()) {
            provider.setParamsSchemaJson(
                    JsonUtil.objectToJsonString(
                            data.getParamsSchema().stream().map(
                                    JsonUtil::thriftBaseToJsonNode).collect(Collectors.toList())
                    )
            );
        }

        if (data.isSetAccounts()) {
            Map<String, Long> accountsMap = data.getAccounts().entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().getSymbolicCode(), e -> e.getValue().getSettlement()));
            provider.setAccountsJson(JsonUtil.objectToJsonString(accountsMap));
        }
        provider.setCurrent(current);
        return provider;
    }
}
