package de.rwth.idsg.steve.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.rwth.idsg.steve.ocpp.OcppTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargePointSelectReq {
    @JsonProperty("ocppTransport")
    private OcppTransport ocppTransport;
    @JsonProperty("chargeBoxId")
    private String chargeBoxId;
    @JsonProperty("endpointAddress")
    private String endpointAddress;
}