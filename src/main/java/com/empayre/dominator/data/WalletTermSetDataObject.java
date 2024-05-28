package com.empayre.dominator.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletTermSetDataObject {

    private Long id;
    private String partyId;
    private String identityId;
    private String contractId;
    private String currency;
    private String walletId;
    private String walletName;
    private Integer termSetId;
    private Long contractRecordId;
}
