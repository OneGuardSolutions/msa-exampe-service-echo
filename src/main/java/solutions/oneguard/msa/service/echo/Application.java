/*
 * This file is part of the OneGuard Micro-Service Architecture Echo service example.
 *
 * (c) OneGuard <contact@oneguard.email>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package solutions.oneguard.msa.service.echo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import solutions.oneguard.msa.core.messaging.MessageProducer;
import solutions.oneguard.msa.core.model.Instance;
import solutions.oneguard.msa.core.model.Message;

import java.util.Collections;
import java.util.UUID;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
            .web(WebApplicationType.NONE)
            .run(args);

        Instance instance = context.getBean(Instance.class);
        MessageProducer producer = context.getBean(MessageProducer.class);

        producer.sendToService(
            instance,
            Message.builder()
                .type("echo.request")
                .issuer(instance)
                .payload(Collections.singletonMap("content", "Test message sent to service"))
                .reference(UUID.randomUUID())
                .respondToIssuer(true)
                .build()
        );
        Thread.sleep(1000);
        producer.sendToInstance(
            instance,
            Message.builder()
                .type("echo.request")
                .issuer(instance)
                .payload(Collections.singletonMap("content", "Test message sent to instance"))
                .reference(UUID.randomUUID())
                .build()
        );
    }
}
