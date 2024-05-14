package com.empayre.dominator.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TerminalTermSetDataObject {

    private Long id;
    private Integer terminalId;
    private String terminalName;
    private Integer providerId;
    private String providerName;
    private String currency;
    private String termSetJson;
    private byte[] termSetObject;
    private String paymentTermSetJson;
    private String accountsJson;
    private String walletTermSetJson;
    private List<String> termSetJsonHistory;
}
