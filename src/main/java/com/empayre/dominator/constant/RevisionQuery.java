package com.empayre.dominator.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RevisionQuery {

    public static final String SAVE_SHOPS_QUERY = """
            insert into dmn.shop_revision(obj_id, revision)
            select id, :revision from dmn.shop where party_id = :party_id and current;
            """;

    public static final String SAVE_CONTRACTS_QUERY = """
            insert into dmn.contract_revision(obj_id, revision)
            select id, :revision from dmn.contract where party_id = :party_id and current;
            """;

    public static final String SAVE_CONTRACTORS_QUERY = """
            insert into dmn.contractor_revision(obj_id, revision)
            select id, :revision from dmn.contractor where party_id = :party_id and current;
            """;
}
