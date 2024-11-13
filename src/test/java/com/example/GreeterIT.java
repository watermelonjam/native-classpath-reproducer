/**
 * Copyright 2024 McMaster University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.example;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;

@Tag("IntegrationTest")
public class GreeterIT {
  static final Logger LOGGER = LoggerFactory.getLogger(GreeterIT.class);
  static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

  static Network network = Network.newNetwork();

  @Container
  static GenericContainer<?> app;

  @SuppressWarnings("resource")
  @BeforeAll
  static void init() {
    app = new GenericContainer<>("native-classpath-reproducer:latest").withNetwork(network)
        .withEnv("QUARKUS_LOG_CATEGORY__COM_EXAMPLE__LEVEL", "DEBUG").withExposedPorts(8080)
        .withLogConsumer(LOG_CONSUMER);
    app.start();
  }

  @AfterAll
  static void teardown() {
    app.stop();
  }

  @Test
  public void givenCorrectOidWhenAuthorizeThenAuthorized() {
    String greeter = String.format("http://%s:%s/greet", app.getHost(), app.getFirstMappedPort());
    given().when().get(greeter).then().statusCode(200);
  }
}
