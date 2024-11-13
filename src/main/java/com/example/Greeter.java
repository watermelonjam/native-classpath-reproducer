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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/greet")
public class Greeter {
  private static final Logger LOG = Logger.getLogger(Greeter.class);

  private List<String> greetings = new ArrayList<>();

  public Greeter() {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("configs");
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
      LOG.debug("Scanning configs classpath folder");
      
      String filename;
      while ((filename = br.readLine()) != null) {
        LOG.debugf("Reading config file %s", filename);
        try (
            InputStream in2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("configs/" + filename);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(in2))) {
          String greeting;
          while ((greeting = br2.readLine()) != null) {
            greetings.add(greeting);
          }
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    
    
  }

  private String getRandomGreeting() {
    Random rand = new Random();
    return greetings.get(rand.nextInt(greetings.size()));
  }

  @GET
  public Uni<Response> greet() {
    return Uni.createFrom().item(Response.ok("Hello " + getRandomGreeting()).build());
  }
}
