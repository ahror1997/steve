/*
 * SteVe - SteckdosenVerwaltung - https://github.com/steve-community/steve
 * Copyright (C) 2013-2023 SteVe Community Team
 * All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.repository.TransactionRepository;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.repository.dto.Transaction;
import de.rwth.idsg.steve.service.ChargePointService16_Client;
import de.rwth.idsg.steve.web.api.ApiControllerAdvice.ApiErrorResponse;
import de.rwth.idsg.steve.web.api.dto.*;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import de.rwth.idsg.steve.web.dto.TransactionQueryForm;
import de.rwth.idsg.steve.web.dto.ocpp.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sevket Goekay <sevketgokay@gmail.com>
 * @since 13.09.2022
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TransactionsRestController {

    private final TransactionRepository transactionRepository;

    @Autowired
    private ChargePointService16_Client chargePointService16_Client;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @PostMapping(value = "/cancel-reserve")
    @ResponseBody
    public TaskIDResp cancelReserve(@RequestBody CancelReservationReq req) {
        log.debug("Read request for query: {}", req);

        CancelReservationParams params = new CancelReservationParams();

        params.setReservationId(req.getReservationId());
        params.setChargePointSelectList(
                req.getChargePointSelectList().stream().map(cp ->
                        new ChargePointSelect(
                                cp.getOcppTransport(), cp.getChargeBoxId(), cp.getEndpointAddress()
                        )
                ).collect(Collectors.toList())
        );

        Integer taskID =
                chargePointService16_Client.cancelReservation(
                        params
                );

        log.debug("Read response for query: {}", taskID);
        return new TaskIDResp(taskID);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @PostMapping(value = "/reserve-now")
    @ResponseBody
    public TaskIDResp reserveNow(@RequestBody ReserveNowReq req) {
        log.debug("Read request for query: {}", req);

        ReserveNowParams params = new ReserveNowParams();

        params.setConnectorId(req.getConnectorId());
        params.setIdTag(req.getIdTag());
        params.setExpiry(req.getExpiry());
        params.setChargePointSelectList(
                req.getChargePointSelectList().stream().map(cp ->
                        new ChargePointSelect(
                                cp.getOcppTransport(), cp.getChargeBoxId(), cp.getEndpointAddress()
                        )
                ).collect(Collectors.toList())
        );

        Integer taskID =
                chargePointService16_Client.reserveNow(
                        params
                );

        log.debug("Read response for query: {}", taskID);
        return new TaskIDResp(taskID);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @PostMapping(value = "/unlock-connector")
    @ResponseBody
    public TaskIDResp unlockConnector(@RequestBody UnlockConnectorReq req) {
        log.debug("Read request for query: {}", req);

        UnlockConnectorParams params = new UnlockConnectorParams();

        params.setConnectorId(req.getConnectorId());
        params.setChargePointSelectList(
                req.getChargePointSelectList().stream().map(cp ->
                        new ChargePointSelect(
                                cp.getOcppTransport(), cp.getChargeBoxId(), cp.getEndpointAddress()
                        )
                ).collect(Collectors.toList())
        );

        Integer taskID =
                chargePointService16_Client.unlockConnector(
                        params
                );

        log.debug("Read response for query: {}", taskID);
        return new TaskIDResp(taskID);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @PostMapping(value = "/start-tx")
    @ResponseBody
    public TaskIDResp startTX(@RequestBody RemoteStartTXReq req) {
        log.debug("Read request for query: {}", req);

        RemoteStartTransactionParams params = new RemoteStartTransactionParams();

        params.setConnectorId(req.getConnectorId());
        params.setIdTag(req.getIdTag());
        params.setChargePointSelectList(
                req.getChargePointSelectList().stream().map(cp ->
                        new ChargePointSelect(
                                cp.getOcppTransport(), cp.getChargeBoxId(), cp.getEndpointAddress()
                        )
                ).collect(Collectors.toList())
        );

        Integer taskID =
                chargePointService16_Client.remoteStartTransaction(
                        params
                );

        log.debug("Read response for query: {}", taskID);
        return new TaskIDResp(taskID);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @PostMapping(value = "/stop-tx")
    @ResponseBody
    public TaskIDResp stopTX(@RequestBody RemoteStopTXReq req) {
        log.debug("Read request for query: {}", req);

        RemoteStopTransactionParams params = new RemoteStopTransactionParams();
        params.setTransactionId(req.getTransactionId());
        params.setChargePointSelectList(
                req.getChargePointSelectList().stream().map(cp ->
                        new ChargePointSelect(
                                cp.getOcppTransport(), cp.getChargeBoxId(), cp.getEndpointAddress()
                        )
                ).collect(Collectors.toList())
        );

        Integer taskID =
                chargePointService16_Client.remoteStopTransaction(
                        params
                );

        log.debug("Read response for query: {}", taskID);
        return new TaskIDResp(taskID);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiErrorResponse.class)}
    )
    @GetMapping(value = "")
    @ResponseBody
    public List<Transaction> get(@Valid TransactionQueryForm.ForApi params) {
        log.debug("Read request for query: {}", params);

        if (params.isReturnCSV()) {
            throw new BadRequestException("returnCSV=true is not supported for API calls");
        }

        var response = transactionRepository.getTransactions(params);
        log.debug("Read response for query: {}", response);
        return response;
    }
}
