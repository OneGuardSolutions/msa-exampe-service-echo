/*
 * This file is part of the OneGuard Micro-Service Architecture Echo service example.
 *
 * (c) OneGuard <contact@oneguard.email>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package solutions.oneguard.msa.service.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import solutions.oneguard.msa.core.messaging.MessageConsumerConfiguration;
import solutions.oneguard.msa.core.messaging.RequestProducer;
import solutions.oneguard.msa.core.model.Instance;
import solutions.oneguard.msa.core.model.Message;

import java.util.Collections;

@SpringBootApplication
@Configuration
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
            .web(WebApplicationType.NONE)
            .run(args);

        Instance instance = context.getBean(Instance.class);
        RequestProducer producer = context.getBean(RequestProducer.class);

        producer.request(Object.class, instance.getService(), Message.builder()
                .type("echo.request")
                .payload(Collections.singletonMap("content", "Test message #1 sent to service"))
                .build()
            )
            .doOnNext(response -> log.info("Received response: <{}>", response.getPayload()))
            .subscribe();
        Thread.sleep(1000);
        producer.request(Object.class, instance.getService(), Message.builder()
                .type("echo.request")
                .payload(Collections.singletonMap("content", "Test message #2 sent to service"))
                .build()
            )
            .doOnNext(response -> log.info("Received response: <{}>", response.getPayload()))
            .subscribe();
    }

    @Bean
    public MessageConsumerConfiguration messageConsumerConfiguration(EchoMessageHandler handler) {
        return new MessageConsumerConfiguration()
            .addHandler("echo.request", handler);
    }
}
