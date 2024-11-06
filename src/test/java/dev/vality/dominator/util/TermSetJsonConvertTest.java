package dev.vality.dominator.util;

import dev.vality.damsel.domain.CashFlowPosting;
import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.damsel.domain.TimedTermSet;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.vality.dominator.generator.TestDataGenerator.createNewTermSetHierarchyObject;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TermSetJsonConvertTest {

    @Test
    void test() throws Exception {
        byte[] serialize = new TSerializer().serialize(createNewTermSetHierarchyObject());
        TermSetHierarchyObject termSetHierarchyObject = new TermSetHierarchyObject();
        new TDeserializer().deserialize(termSetHierarchyObject, serialize);

        assertNotNull(termSetHierarchyObject);
        assertNotNull(termSetHierarchyObject.getData());
        assertNotNull(termSetHierarchyObject.getData().getTermSets());

        TimedTermSet deserializedTermSet = termSetHierarchyObject.getData().getTermSets().get(0);
        assertNotNull(deserializedTermSet);
        assertNotNull(deserializedTermSet.getTerms());
        assertNotNull(deserializedTermSet.getTerms().getPayments());
        assertNotNull(deserializedTermSet.getTerms().getPayments().getFees());
        List<CashFlowPosting> value = deserializedTermSet.getTerms().getPayments().getFees().getValue();
        assertNotNull(value);
        CashFlowPosting cashFlowPosting = value.get(0);
        assertNotNull(cashFlowPosting);
        assertNotNull(cashFlowPosting.getSource());
        assertNotNull(cashFlowPosting.getDestination());
        assertNotNull(cashFlowPosting.getVolume());
        assertNotNull(cashFlowPosting.getVolume().getProduct());
        assertNotNull(cashFlowPosting.getVolume().getProduct().getMaxOf());
    }
}
