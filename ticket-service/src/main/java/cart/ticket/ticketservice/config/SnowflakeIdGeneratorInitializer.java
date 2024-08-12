package cart.ticket.ticketservice.config;

import cart.ticket.ticketservice.domain.model.SnowflakeIdGenerator;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class SnowflakeIdGeneratorInitializer implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() throws UnknownHostException {
        return new SnowflakeIdGenerator(serverPort);
    }
}