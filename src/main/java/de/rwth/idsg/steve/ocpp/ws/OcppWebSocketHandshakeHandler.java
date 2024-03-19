package de.rwth.idsg.steve.ocpp.ws;

import de.rwth.idsg.steve.config.WebSocketConfiguration;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ocpp.cs._2015._10.RegistrationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.rwth.idsg.steve.utils.StringUtils.getLastBitFromUrl;

@Slf4j
@RequiredArgsConstructor
public class OcppWebSocketHandshakeHandler implements HandshakeHandler {

    private final DefaultHandshakeHandler delegate;
    private final List<AbstractWebSocketEndpoint> endpoints;
    private final ChargePointHelperService chargePointHelperService;

    // Default OCPP version to use if none is specified
    private static final String DEFAULT_OCPP_VERSION = "ocpp1.6";

    public WebSocketHandler getDummyWebSocketHandler() {
        return new TextWebSocketHandler();
    }

    @Override
    public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {

        String chargeBoxId = getLastBitFromUrl(request.getURI().getPath());
        Optional<RegistrationStatus> status = chargePointHelperService.getRegistrationStatus(chargeBoxId);

        boolean allowConnection = status.isPresent();

        if (!allowConnection) {
            log.error("ChargeBoxId '{}' is not recognized.", chargeBoxId);
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return false;
        }

        attributes.put(AbstractWebSocketEndpoint.CHARGEBOX_ID_KEY, chargeBoxId);

        List<String> requestedProtocols = new WebSocketHttpHeaders(request.getHeaders()).getSecWebSocketProtocol();

        // Use a default OCPP version if none is specified
        if (CollectionUtils.isEmpty(requestedProtocols)) {
            log.warn("No protocol (OCPP version) is specified. Using default version: {}", DEFAULT_OCPP_VERSION);
            requestedProtocols = List.of(DEFAULT_OCPP_VERSION);
        }

        AbstractWebSocketEndpoint endpoint = selectEndpoint(requestedProtocols);

        if (endpoint == null) {
            log.error("None of the requested protocols '{}' is supported", requestedProtocols);
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return false;
        }

        log.debug("ChargeBoxId '{}' will be using {}", chargeBoxId, endpoint.getClass().getSimpleName());
        return delegate.doHandshake(request, response, endpoint, attributes);
    }

    private AbstractWebSocketEndpoint selectEndpoint(List<String> requestedProtocols) {
        for (String requestedProtocol : requestedProtocols) {
            for (AbstractWebSocketEndpoint item : endpoints) {
                if (item.getVersion().getValue().equals(requestedProtocol)) {
                    return item;
                }
            }
        }
        return null;
    }
}