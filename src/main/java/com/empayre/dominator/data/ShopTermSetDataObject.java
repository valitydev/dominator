package com.empayre.dominator.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopTermSetDataObject {

    private Long id;
    private String partyId;
    private String shopId;
    private String contractId;
    private String currency;
    private String shopName;
    private Integer termSetId;
    private Long contractRecordId;
}
