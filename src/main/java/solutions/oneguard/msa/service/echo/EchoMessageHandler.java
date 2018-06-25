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
import solutions.oneguard.msa.core.model.Instance;
import solutions.oneguard.msa.core.model.Message;

@Component
public class EchoMessageHandler extends AbstractMessageHandler<Object> {
    private final MessageProducer producer;
    private final Instance instance;

    @Autowired
    public EchoMessageHandler(MessageProducer producer, Instance instance) {
        super(Object.class);
        this.producer = producer;
        this.instance = instance;
    }

    public void handleMessage(Object payload, Message originalMessage) {
        Message response = Message.builder()
            .type("echo.response")
            .issuer(instance)
            .principal(originalMessage.getPrincipal())
            .payload(payload)
            .reference(originalMessage.getReference())
            .build();

        if (originalMessage.isRespondToIssuer()) {
            producer.sendToInstance(originalMessage.getIssuer(), response);
        } else {
            producer.sendToService(originalMessage.getIssuer(), response);
        }
    }
}
