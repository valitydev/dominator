package dev.vality.dominator.handler.dominant;

import dev.vality.dominator.dao.dominant.iface.DomainObjectDao;
import dev.vality.damsel.domain.DomainObject;
import dev.vality.damsel.domain_config.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    public abstract C convertToDatabaseObject(T object, Long versionId, LocalDateTime createdAt, boolean current);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(Operation operation, LocalDateTime createdAt, Long versionId) {
        T object = getTargetObject();
        if (operation.isSetInsert()) {
            insertDomainObject(object, createdAt, versionId);
        } else if (operation.isSetUpdate()) {
            updateDomainObject(object, createdAt, versionId);
        } else if (operation.isSetRemove()) {
            removeDomainObject(object, createdAt, versionId);
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
    public void insertDomainObject(T object, LocalDateTime createdAt, Long versionId) {
        log.debug("Start to insert '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, createdAt, true));
        log.debug("End to insert '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDomainObject(T object, LocalDateTime createdAt, Long versionId) {
        log.debug("Start to update '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
        getDomainObjectDao().updateNotCurrent(getTargetObjectRefId());
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, createdAt, true));
        log.debug("End to update '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeDomainObject(T object, LocalDateTime createdAt, Long versionId) {
        log.debug("Start to remove '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
        getDomainObjectDao().updateNotCurrent(getTargetObjectRefId());
        getDomainObjectDao().save(convertToDatabaseObject(object, versionId, createdAt, false));
        log.debug("End to remove '{}' (id={}, versionId={}, createdAt={})", object.getClass().getSimpleName(),
                getTargetObjectRefId(), versionId, createdAt);
    }
}
