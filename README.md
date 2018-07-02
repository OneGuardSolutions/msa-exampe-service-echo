# Echo service example for OneGuard Micro-Service Architecture

[![Build Status](https://travis-ci.org/OneGuardSolutions/msa-exampe-service-echo.svg?branch=master)](https://travis-ci.org/OneGuardSolutions/msa-exampe-service-echo)
[![GitHub license](https://img.shields.io/github/license/OneGuardSolutions/msa-exampe-service-echo.svg)](https://github.com/OneGuardSolutions/msa-exampe-service-echo/blob/master/LICENSE)
[![Maintainability](https://api.codeclimate.com/v1/badges/307482e5d00fdc4b5477/maintainability)](https://codeclimate.com/github/OneGuardSolutions/msa-exampe-service-echo/maintainability)

This is an example of service for OneGuard Micro-Service Architecture.

## Requirements:

- JDK 8 or later
- Maven 3.2+
- RabbitMQ server

## How to run this example

Before we can build our micro-service, we need to set up the server
that will handle receiving and sending messages.

RabbitMQ server is freely available at
[http://www.rabbitmq.com/download.html](http://www.rabbitmq.com/download.html).

On Debian / Ubuntu you can install the server with apt:

```bash
sudo apt-get install rabbitmq-server
```

Default configuration (localhost, default port & authentication) will work for this example.

If your environment differs, override the configuration in `src/main/resources/application-default.yml`.
See [`src/main/resources/application.yml`](src/main/resources/application.yml) for reference. 

Clone this repository:

```bash
git clone git@github.com:OneGuardSolutions/msa-exampe-service-echo.git
```

Start the app from the cloned directory:

```bash
mvn spring-boot:run
```

You should see lines like these at the bottom of the log output:

```
2018-07-02 16:37:25.649  INFO 22172 --- [  elastic-3] : Sending request: <{content=Test message #2 sent to service}>
2018-07-02 16:37:25.649  INFO 22172 --- [  elastic-5] : Sending request: <{content=Test message #4 sent to service}>
2018-07-02 16:37:25.649  INFO 22172 --- [  elastic-4] : Sending request: <{content=Test message #3 sent to service}>
2018-07-02 16:37:25.649  INFO 22172 --- [  elastic-2] : Sending request: <{content=Test message #1 sent to service}>
2018-07-02 16:37:25.649  INFO 22172 --- [  elastic-6] : Sending request: <{content=Test message #5 sent to service}>
2018-07-02 16:37:25.739  INFO 22172 --- [container-1] : Received response: <{content=Test message #1 sent to service}>
2018-07-02 16:37:25.740  INFO 22172 --- [container-1] : Received response: <{content=Test message #5 sent to service}>
2018-07-02 16:37:25.741  INFO 22172 --- [container-1] : Received response: <{content=Test message #4 sent to service}>
2018-07-02 16:37:25.741  INFO 22172 --- [container-1] : Received response: <{content=Test message #3 sent to service}>
2018-07-02 16:37:25.741  INFO 22172 --- [container-1] : Received response: <{content=Test message #2 sent to service}>
``` 

Those are test messages the service sent to itself after the startup.

See [OneGuard Micro-Service Architecture Inter-Service Communication Protocol Specification][1]
for more information on how it works.

## Used libraries

- [OneGuard Micro-Service Architecture Core library](https://github.com/OneGuardSolutions/msa-core)
- [Spring Boot](https://spring.io/projects/spring-boot)

[1]: https://github.com/OneGuardSolutions/msa-interservice-communication-protocol-specification
