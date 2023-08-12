package de.rwth.idsg.steve.repository.dto;

import de.rwth.idsg.steve.ocpp.OcppTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoteStartTXReq {
    private Integer connectorId;
    private String idTag;
    private List<ChargePointSelectReq> chargePointSelectList;
}

