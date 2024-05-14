package com.empayre.dominator.handler.dominant.impl;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.dao.dominant.impl.ProviderDaoImpl;
import com.empayre.dominator.domain.tables.pojos.Provider;
import com.empayre.dominator.exception.SerializationException;
import com.empayre.dominator.handler.dominant.AbstractDominantHandler;
import com.empayre.dominator.util.JsonUtil;
import dev.vality.damsel.domain.PaymentsProvisionTerms;
import dev.vality.damsel.domain.ProviderObject;
import dev.vality.damsel.domain.ProviderParameter;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProviderHandler extends AbstractDominantHandler<ProviderObject, Provider, Integer> {

    private final ProviderDaoImpl providerDao;
    private final TSerializer serializer;

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
            PaymentsProvisionTerms provisionTerms = data.getTerms().getPayments();
            provider.setPaymentTermsJson(JsonUtil.thriftBaseToJsonString(provisionTerms));
            provider.setPaymentTermsObject(serialize(provisionTerms));
        } else if (data.isSetPaymentTerms()) {
            PaymentsProvisionTerms paymentTerms = data.getPaymentTerms();
            provider.setPaymentTermsJson(JsonUtil.thriftBaseToJsonString(paymentTerms));
            provider.setPaymentTermsObject(serialize(paymentTerms));
        }

        if (data.isSetTerms() && data.getTerms().isSetRecurrentPaytools()) {
            provider.setRecurrentPaytoolTermsJson(
                    JsonUtil.thriftBaseToJsonString(data.getTerms().getRecurrentPaytools()));
            provider.setRecurrentPaytoolTermsObject(serialize(data.getTerms().getRecurrentPaytools()));
        } else if (data.isSetRecurrentPaytoolTerms()) {
            provider.setRecurrentPaytoolTermsJson(JsonUtil.thriftBaseToJsonString(data.getRecurrentPaytoolTerms()));
            provider.setRecurrentPaytoolTermsObject(serialize(data.getRecurrentPaytoolTerms()));
        }

        if (data.isSetIdentity()) {
            provider.setIdentity(data.getIdentity());
        }
        if (data.isSetTerms() && data.getTerms().isSetWallet()) {
            provider.setWalletTermsJson(JsonUtil.thriftBaseToJsonString(data.getTerms().getWallet()));
            provider.setWalletTermsObject(serialize(data.getTerms().getWallet()));
        }
        if (data.isSetParamsSchema()) {
            List<ProviderParameter> paramsSchema = data.getParamsSchema();
            provider.setParamsSchemaJson(JsonUtil.objectToJsonString(
                            paramsSchema.stream()
                                    .map(JsonUtil::thriftBaseToJsonNode)
                                    .collect(Collectors.toList()))
            );
            byte[][] params = new byte[paramsSchema.size()][];
            for (int i = 0; i < paramsSchema.size(); i++) {
                params[i] = serialize(paramsSchema.get(i));
            }
            provider.setParamsSchemaObject(params);
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

    private byte[] serialize(TBase<?, ?> base) {
        try {
            return serializer.serialize(base);
        } catch (TException e) {
            throw new SerializationException(e);
        }
    }
}
