# Echo service example for OneGuard Micro-Service Architecture

This is an example of service for OneGuard Micro-Service Architecture.

## Requirements:

- JDK 9 or above
- Maven
- RabbitMQ server

## How to run this example

First install [RabbitMQ Server](http://www.rabbitmq.com/download.html).
For purpose of this example we assume the server is running on local host and default port.

If your environment differs, override the configuration in `src/main/resources/application.yml`.
See `src/main/resources/application.default.yml` for reference. 

Clone this repository:

```bash
git clone git@github.com:OneGuardSolutions/msa-exampe-service-echo.git
```

Start the app from the clone directory:

```bash
mvn spring-boot:run
```

You should see lines like these at the bottom of the log output:

```
2018-06-22 17:59:50.369  INFO 15670 --- [    container-1] s.o.msa.core.messaging.MessageConsumer   : Received message with no handler: <Message(type=echo.response, principal=null, issuer=Instance(service=echo, id=9c5a89e6-af13-4fa3-8afe-bbe8204f37a0), payload={content=Test message sent to service}, occuredAt=Fri Jun 22 17:59:50 CEST 2018, reference=7e0d8d6a-2dfa-4cca-aa64-9b6661f5cd34, respondToIssuer=false)>
2018-06-22 17:59:51.336  INFO 15670 --- [    container-1] s.o.msa.core.messaging.MessageConsumer   : Received message with no handler: <Message(type=echo.response, principal=null, issuer=Instance(service=echo, id=9c5a89e6-af13-4fa3-8afe-bbe8204f37a0), payload={content=Test message sent to instance}, occuredAt=Fri Jun 22 17:59:51 CEST 2018, reference=eeb14024-8060-497a-9f39-7f48ffe02379, respondToIssuer=false)>
``` 

Those are test messages the service sent to itself after the startup.

## Message format

The message MUST by json encoded.

```json
{
  "type": "echo.request",
  "principal": null,
  "issuer": {
    "service": "echo",
    "id": "268f66e0-af97-40ad-8268-9276d3263b94" 
  },
  "payload": {},
  "occurredAt": 1514761200000, 
  "reference": "any string",  
  "respondToIssuer": false 
}
```

- **`type`** `string` - message type used to determine handler
- **`principal`** `object` *optional* - security principal in whose name the message is published
- **`issuer`** - issuing service instance
  - **`service`** `string` - service name
  - **`id`** `uuid` - instance id
- **`payload`** `object` - actual contents of the message processed by the handler, 
                           structure depends on the handler
- **`occurredAt`** `date` *optional* - time of message occurrence in milliseconds since the epoch 
                                      (1970-01-01 00:00:00.000); defaults to current time when received
- **`reference`** `string` *optional* - used by client to match any potential response to the request
- **`respondToIssuer`** `boolean` *optional* - if `true` any response is to be sent to the specific instance
                                               that issued the request,
                                               otherwise it is to be sent to the service; default to `false`

The AMQP message MUST contain these properties:

```json
{
  "content_type": "application/json",
  "headers": {
    "__TypeId__": "solutions.oneguard.msa.core.model.Message"
  }
}
```

- **`content_type`** - specifies message encoding; currently only `application/json` is supported
- **`headers`** - custom message headers
  - **`__TypeId__`** - specifies type of payload container; 
                       currently only `solutions.oneguard.msa.core.model.Message` is supported



## Sending custom messages

You can use [`rabbitmqadmin`](https://www.rabbitmq.com/management-cli.html) to send messages from the terminal:

```bash
rabbitmqadmin publish \
    exchange=service-echo \
    routing_key=anything \
    payload='{"type":"echo.request","issuer":{"service":"echo","id":"268f66e0-af97-40ad-8268-9276d3263b94"},"payload":{"content":"Custom test message"}}' \
    properties='{"content_type":"application/json","headers":{"__TypeId__":"solutions.oneguard.msa.core.model.Message"}}'
```

## Used libraries

- [OneGuard Micro-Service Architecture Core library](https://github.com/OneGuardSolutions/msa-core)
- [Spring Boot](https://spring.io/projects/spring-boot)
