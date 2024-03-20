# SIEM Logger SPI for Keycloak

This repository implements a simple SPI to send Keycloak events to an SIEM application.

# Environment Variables
For this SPI to work, the following environment variables must be defined on the Keycloak server.

| Variable                      | Value example | Description |
| -----------------             | ---------     | ----------------------------------------- |
| EVENT_LISTENER_SIEM_HOST      | netcat        | IP or hostname of the SIEM server         |
| EVENT_LISTENER_SIEM_PORT      | 514           | Port that SIEM server will be listening   |
| EVENT_LISTENER_SIEM_APP_NAME  | Keycloak      | Application name                          |

> Here I used the Syslog Java Client for send the messages to SIEM. For this, I used the `TcpSyslogMessageSender` if you want to send UDP messages check the ![Syslog Java Client docs](https://github.com/jenkinsci/syslog-java-client) to see how to do it.

# Testing the SPI
I created a docker-compose file to allow testing this SPI locally.

Here, the Keycloak with the SPI will run in a container and to simulate our SIEM I've used [netcat](https://docs.oracle.com/cd/E86824_01/html/E54763/netcat-1.html).

## Dependencies
- Docker
- Java
- Maven
---

1. Cloning repository
   ```bash
   git clone https://github.com/lspaulucio/keycloak-siem-spi
   cd keycloak-siem-spi/sample_event_listener
   ```
2. Creating jar file
   ```bash
   mvn package -f "pom.xml"
   ```
3. Running Keycloak. 
> This will build the SPI and insert it into Keycloak. Check Dockerfile

> In this version of Keycloak (18.0.2-legacy) the customized SPIs are localized in the `/opt/jboss/keycloak/standalone/deployments/` folder.
   ```bash
   docker compose up
   ```
   
6. Activating SIEM Logger SPI
   
   6.1 Log in to Keycloak admin console [http://localhost:8080](http://localhost:8080) (admin, admin)
   
   6.2 Go to **Realm settings->Events**. In the field **"Event Listeners"** includes **"siem_logger"** and click in **Save**.

![KeycloakConfig](https://github.com/lspaulucio/keycloak-siem-spi/assets/17748220/559e70f4-7257-48da-9612-d3e3c7a7c634)


> **Attention**: In this example, I am configuring the event listener on Master Realm.

> If it is not the realm that you want to send the events to SIEM, select the realm you want and include the **siem_logger** in it.

> If you want to send events from more than one realm, you should configure and insert the **siem_logger** as an event listener in all of them.

7. Open netcat logs (Here netcat will work as our SIEM server).
   ```bash
   docker logs netcat -f
   ```

8. Now let's create a new client on Master Realm and check if the events will be received in netcat.
   
   8.1 Go to **Clients->Create**. 

   8.2 Insert a name for your Client ID and click on **Save**.

9. Get back to netcat logs. If everything works fine will see the create realm event on netcat logs :tada: :tada: :tada: :smile:
  ![netcatlogs](https://github.com/lspaulucio/keycloak-siem-spi/assets/17748220/8a814a4d-29d8-4a73-b94a-6d100589e1e7)

    > If you are not able to see the logs, please recheck the steps above and see if you do everything right :grimacing: :sweat_smile:
    
  
# Credits

This SPI is based on Adwait Thattey repository [keycloak-event-listener-spi](https://github.com/lspaulucio/keycloak-event-listener-spi).

He created a post on his blog explaining step by step on how to create an SPI. If you want more information check it out: https://dev.to/adwaitthattey/building-an-event-listener-spi-plugin-for-keycloak-2044

