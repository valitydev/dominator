package com.empayre.dominator.util;

import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.damsel.domain.TermSetHierarchyRef;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;

@UtilityClass
public class TermSetConverterUtils {

    public static final String EMPTY = "";

    public static String replaceNull(String source) {
        return source == null ? EMPTY : source;
    }

    public static TermSetHierarchyObject createEmptyTermSetHierarchyObject() {
        return new TermSetHierarchyObject()
                .setRef(new TermSetHierarchyRef().setId(-1))
                .setData(new dev.vality.damsel.domain.TermSetHierarchy()
                        .setName("-")
                        .setDescription("-")
                        .setTermSets(new ArrayList<>())
                );
    }
}
