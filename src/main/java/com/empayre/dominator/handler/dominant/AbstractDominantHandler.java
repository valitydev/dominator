package com.empayre.dominator.handler.dominant;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import dev.vality.damsel.domain.DomainObject;
import dev.vality.damsel.domain_config.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Getter
@Setter
public abstract class AbstractDominantHandler<T, C, I> implements DominantHandler<Operation> {

    private static final String UNKNOWN_TYPE_EX = "Unknown type of operation. Only insert/update/remove supports. " +
            "Operation: ";

    private DomainObject domainObject;

    protected abstract DomainObjectDao<C, I> getDomainObjectDao();

    protected abstract T getTargetObject();

    protected abstract I getTargetObjectRefId();

    protected abstract boolean acceptDomainObject();

    public abstract C convertToDatabaseObject(T object, Long versionId, boolean current);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(Operation operation, Long versionId) {
        T object = getTargetObject();
        if (operation.isSetInsert()) {
            insertDomainObject(object, versionId);
        } else if (operation.isSetUpdate()) {
            updateDomainObject(object, versionId);
        } else if (operation.isSetRemove()) {
            removeDomainObject(object, versionId);
        } else {
            throw new IllegalStateException(
                    UNKNOWN_TYPE_EX + operation);
        }
    }

    @Override
    public boolean acceptAndSet(Operation operation) {
        if (operation.isSetInsert()) {
            setDomainObject(operation.getInsert().getObject());
        } else if (operation.isSetUpdate()) {
            setDomainObject(operation.getUpdate().getNewObject());
        } else if (operation.isSetRemove()) {
            setDomainObject(operation.getRemove().getObject());
        } else {
            throw new IllegalStateException(
                    UNKNOWN_TYPE_EX + operation);
        }
        return acceptDomainObject();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertDomainObject(T object, Long versionId) {
        log.debug("Start to insert '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, true));
        log.debug("End to insert '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDomainObject(T object, Long versionId) {
        log.debug("Start to update '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
        getDomainObjectDao().updateNotCurrent(getTargetObjectRefId());
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, true));
        log.debug("End to update '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeDomainObject(T object, Long versionId) {
        log.debug("Start to remove '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
        getDomainObjectDao().updateNotCurrent(getTargetObjectRefId());
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, false));
        log.debug("End to remove '{}' with id={}, versionId={}", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId);
    }
}
