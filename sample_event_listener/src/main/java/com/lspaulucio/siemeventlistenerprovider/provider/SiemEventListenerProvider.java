package com.lspaulucio.siemeventlistenerprovider.provider;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.io.IOException;
import java.util.Map;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;

public class SiemEventListenerProvider implements EventListenerProvider {

    private static final TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();

    public SiemEventListenerProvider() {
        // messageSender.setDefaultMessageHostname("myhostname");
        messageSender.setDefaultAppName(System.getenv("EVENT_LISTENER_SIEM_APP_NAME"));
        messageSender.setDefaultFacility(Facility.AUDIT);
        messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
        messageSender.setSyslogServerHostname(System.getenv("EVENT_LISTENER_SIEM_HOST"));
        messageSender.setSyslogServerPort(Integer.parseInt(System.getenv("EVENT_LISTENER_SIEM_PORT")));
        messageSender.setMessageFormat(MessageFormat.RFC_5424);
    }

    @Override
    public void onEvent(Event event) {

        try {
            messageSender.sendMessage(toString(event));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

        try {
            messageSender.sendMessage(toString(adminEvent));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            messageSender.sendMessage("This is a test message\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}