package de.rwth.idsg.steve.web.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoteStopTXReq {
    private Integer transactionId;
    private List<ChargePointSelectReq> chargePointSelectList;
}

