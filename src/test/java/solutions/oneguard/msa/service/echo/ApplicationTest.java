/*
 * This file is part of the OneGuard Micro-Service Architecture Echo service example.
 *
 * (c) OneGuard <contact@oneguard.email>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package solutions.oneguard.msa.service.echo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import solutions.oneguard.msa.core.messaging.RequestProducer;
import solutions.oneguard.msa.core.model.Instance;
import solutions.oneguard.msa.core.model.Message;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.main.banner-mode=off"})
public class ApplicationTest {
    @Autowired
    private RequestProducer producer;

    @Autowired
    private Instance currentInstance;

    @Test
    public void request() {
        Object payload = Collections.singletonMap("content", "Test message sent to service");
        Message<Object> response = producer.request(
                Object.class,
                currentInstance.getService(),
                Message.builder().type("echo.request").payload(payload).build()
            )
            .block(Duration.of(5, ChronoUnit.SECONDS));

        assertEquals(payload, response.getPayload());
    }
}
