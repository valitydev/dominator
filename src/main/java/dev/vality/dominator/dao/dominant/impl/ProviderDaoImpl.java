package dev.vality.dominator.dao.dominant.impl;

import dev.vality.dominator.dao.AbstractDao;
import dev.vality.dominator.dao.dominant.iface.DomainObjectDao;
import dev.vality.dominator.domain.tables.pojos.Provider;
import dev.vality.dominator.exception.DaoException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.dominator.domain.Tables.PROVIDER;

@Component
public class ProviderDaoImpl extends AbstractDao implements DomainObjectDao<Provider, Integer> {

    public ProviderDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long save(Provider provider) throws DaoException {
        return getDslContext()
                .insertInto(PROVIDER)
                .set(getDslContext().newRecord(PROVIDER, provider))
                .returning(PROVIDER.ID)
                .fetchOne()
                .getId();
    }

    @Override
    public void updateNotCurrent(Integer providerId) throws DaoException {
        getDslContext()
                .update(PROVIDER)
                .set(PROVIDER.CURRENT, false)
                .where(PROVIDER.PROVIDER_REF_ID.eq(providerId)
                        .and(PROVIDER.CURRENT))
                .execute();
    }
}
