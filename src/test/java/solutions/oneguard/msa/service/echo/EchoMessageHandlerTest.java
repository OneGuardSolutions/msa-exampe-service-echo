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

import solutions.oneguard.msa.core.messaging.MessageProducer;
import solutions.oneguard.msa.core.model.Message;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EchoMessageHandlerTest {
    @Test
    public void handleMessage() {
        MessageProducer producer = mock(MessageProducer.class);
        EchoMessageHandler handler = new EchoMessageHandler(producer);

        Object payload = Collections.singletonMap("testProperty", "testPayload");
        Message<Object> originalMessage = Message.builder()
            .id(UUID.randomUUID())
            .type("echo.request")
            .payload(payload)
            .respondToInstance(true)
            .build();

        handler.handleMessage(originalMessage);
        verify(producer).sendResponse(same(originalMessage), eq("echo.response"), same(payload));
    }
}
