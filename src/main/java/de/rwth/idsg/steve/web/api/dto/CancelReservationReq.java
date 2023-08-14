package de.rwth.idsg.steve.web.api.dto;

import de.rwth.idsg.steve.web.api.dto.ChargePointSelectReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservationReq {
    private Integer reservationId;
    private List<ChargePointSelectReq> chargePointSelectList;
}
