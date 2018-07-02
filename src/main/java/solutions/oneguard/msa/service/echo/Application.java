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
import reactor.core.scheduler.Schedulers;

import solutions.oneguard.msa.core.messaging.MessageConsumerConfiguration;
import solutions.oneguard.msa.core.messaging.RequestProducer;
import solutions.oneguard.msa.core.model.Instance;
import solutions.oneguard.msa.core.model.Message;

import java.util.Collections;

@SpringBootApplication
@Configuration
public class Application {
    private static final String ECHO_REQUEST_TYPE = "echo.request";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * Starts the application.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
            .web(WebApplicationType.NONE)
            .run(args);

        Instance instance = context.getBean(Instance.class);
        RequestProducer producer = context.getBean(RequestProducer.class);

        for (int i = 1; i <= 5; i++) {
            Object payload = Collections.singletonMap("content", String.format("Test message #%d sent to service", i));
            producer.request(
                Object.class,
                instance.getService(),
                Message.builder()
                    .type(ECHO_REQUEST_TYPE)
                    .payload(payload)
                    .build()
                )
                .doOnSubscribe(subscription -> log.info("Sending request: <{}>", payload))
                .subscribeOn(Schedulers.elastic())
                .subscribe(response -> log.info("Received response: <{}>", response.getPayload()));
        }
    }

    @Bean
    public MessageConsumerConfiguration messageConsumerConfiguration(EchoMessageHandler handler) {
        return new MessageConsumerConfiguration()
            .addHandler(ECHO_REQUEST_TYPE, handler);
    }
}
