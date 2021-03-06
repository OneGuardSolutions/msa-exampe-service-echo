/*
 * This file is part of the OneGuard Micro-Service Architecture Echo service example.
 *
 * (c) OneGuard <contact@oneguard.email>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package solutions.oneguard.msa.service.echo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import solutions.oneguard.msa.core.messaging.AbstractMessageHandler;
import solutions.oneguard.msa.core.messaging.MessageProducer;
import solutions.oneguard.msa.core.model.Message;

@Component
public class EchoMessageHandler extends AbstractMessageHandler<Object> {
    private final MessageProducer producer;

    @Autowired
    public EchoMessageHandler(MessageProducer producer) {
        super(Object.class);
        this.producer = producer;
    }

    @Override
    public void handleMessage(Message<Object> originalMessage) {
        producer.sendResponse(originalMessage, "echo.response", originalMessage.getPayload());
    }
}
