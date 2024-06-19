package com.empayre.dominator.handler.dominant.impl;

import com.empayre.dominator.dao.dominant.iface.DomainObjectDao;
import com.empayre.dominator.dao.party.impl.TermSetHierarchyDaoImpl;
import com.empayre.dominator.domain.tables.pojos.TermSetHierarchy;
import com.empayre.dominator.exception.SerializationException;
import com.empayre.dominator.handler.dominant.AbstractDominantHandler;
import com.empayre.dominator.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import dev.vality.damsel.domain.TermSetHierarchyObject;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TermSetHierarchyHandler
        extends AbstractDominantHandler<TermSetHierarchyObject, TermSetHierarchy, Integer> {

    private final TermSetHierarchyDaoImpl termSetHierarchyDao;
    private final TSerializer serializer;

    @Override
    protected DomainObjectDao<TermSetHierarchy, Integer> getDomainObjectDao() {
        return termSetHierarchyDao;
    }

    @Override
    protected TermSetHierarchyObject getTargetObject() {
        return getDomainObject().getTermSetHierarchy();
    }

    @Override
    protected Integer getTargetObjectRefId() {
        return getTargetObject().getRef().getId();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetTermSetHierarchy();
    }

    @Override
    public TermSetHierarchy convertToDatabaseObject(TermSetHierarchyObject termSetHierarchyObject,
                                                    Long versionId,
                                                    LocalDateTime createdAt,
                                                    boolean current) {
        TermSetHierarchy termSetHierarchy = new TermSetHierarchy();
        termSetHierarchy.setVersionId(versionId);
        termSetHierarchy.setCreatedAt(createdAt);
        termSetHierarchy.setTermSetHierarchyRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.TermSetHierarchy data = termSetHierarchyObject.getData();
        termSetHierarchy.setName(data.getName());
        termSetHierarchy.setDescription(data.getDescription());
        if (data.isSetParentTerms()) {
            termSetHierarchy.setParentTermsRefId(data.getParentTerms().getId());
        }
        List<JsonNode> jsonNodes = data.getTermSets().stream()
                .map(JsonUtil::thriftBaseToJsonNode)
                .collect(Collectors.toList());
        termSetHierarchy.setTermSetsJson(JsonUtil.objectToJsonString(jsonNodes));
        try {
            termSetHierarchy.setTermSetHierarchyObject(serializer.serialize(termSetHierarchyObject));
        } catch (TException e) {
            throw new SerializationException(e);
        }
        termSetHierarchy.setCurrent(current);
        return termSetHierarchy;
    }
}
