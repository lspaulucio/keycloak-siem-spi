package com.lspaulucio.siemeventlistenerprovider.provider;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.util.Map;

// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class SiemEventListenerProvider implements EventListenerProvider {

    private static final Logger logger = LogManager.getLogger(SiemEventListenerProvider.class);

    public SiemEventListenerProvider() {
    }

    @Override
    public void onEvent(Event event) {

        logger.info(toString(event));
        // System.out.println("Event Occurred MyListener:" + toString(event));
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

        logger.info(toString(adminEvent));
        // System.out.println("Admin Event Occurred MyListener:" +
        // toString(adminEvent));
    }

    @Override
    public void close() {

    }

    private String toString(Event event) {

        StringBuilder sb = new StringBuilder();

        sb.append("type=");

        sb.append(event.getType());

        sb.append(", realmId=");

        sb.append(event.getRealmId());

        sb.append(", clientId=");

        sb.append(event.getClientId());

        sb.append(", userId=");

        sb.append(event.getUserId());

        sb.append(", ipAddress=");

        sb.append(event.getIpAddress());

        if (event.getError() != null) {

            sb.append(", error=");

            sb.append(event.getError());

        }

        if (event.getDetails() != null) {

            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {

                sb.append(", ");

                sb.append(e.getKey());

                if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {

                    sb.append("=");

                    sb.append(e.getValue());

                } else {

                    sb.append("='");

                    sb.append(e.getValue());

                    sb.append("'");

                }

            }

        }

        return sb.toString();

    }

    private String toString(AdminEvent adminEvent) {

        StringBuilder sb = new StringBuilder();

        sb.append("operationType=");

        sb.append(adminEvent.getOperationType());

        sb.append(", realmId=");

        sb.append(adminEvent.getAuthDetails().getRealmId());

        sb.append(", clientId=");

        sb.append(adminEvent.getAuthDetails().getClientId());

        sb.append(", userId=");

        sb.append(adminEvent.getAuthDetails().getUserId());

        sb.append(", ipAddress=");

        sb.append(adminEvent.getAuthDetails().getIpAddress());

        sb.append(", resourcePath=");

        sb.append(adminEvent.getResourcePath());

        if (adminEvent.getError() != null) {

            sb.append(", error=");

            sb.append(adminEvent.getError());

        }
        return sb.toString();

    }

    public static void main(String[] args) {
        System.out.println("Test LOGGER");
        System.out.println(logger.getName());
        logger.info("Test INFO");
        logger.fatal("Teste FATAL");
        logger.error("Teste ERROR");
        logger.debug("Teste DEBUG");
        logger.trace("Teste TRACE");
    }

}