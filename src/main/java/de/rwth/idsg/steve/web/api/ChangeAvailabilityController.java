package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.service.ChargePointService12_Client;
import de.rwth.idsg.steve.web.dto.ocpp.ChangeAvailabilityParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/change-availability", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChangeAvailabilityController {


    @Autowired
    @Qualifier("ChargePointService12_Client")
    private ChargePointService12_Client chargePointService12_Client;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiControllerAdvice.ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiControllerAdvice.ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiControllerAdvice.ApiErrorResponse.class)}
    )
    @PostMapping(value = "")
    @ResponseBody
    public Integer change(@RequestBody ChangeAvailabilityParams params) {
        log.debug("Read request for query: {}", params);

        Integer i = chargePointService12_Client.changeAvailability(params);
        log.debug("Read response for query: {}", i);
        return i;
    }

}
