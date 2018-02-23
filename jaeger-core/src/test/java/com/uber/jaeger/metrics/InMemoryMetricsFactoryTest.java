/*
 * Copyright (c) 2017, The Jaeger Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.uber.jaeger.metrics;

import static org.junit.Assert.assertEquals;

import com.uber.jaeger.Tracer;
import com.uber.jaeger.reporters.InMemoryReporter;
import com.uber.jaeger.samplers.ConstSampler;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class InMemoryMetricsFactoryTest {

  @Test
  public void metricNameIsUsedForCounter() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    inMemoryMetricsFactory.createCounter("thecounter", tags);

    assertEquals(-1, inMemoryMetricsFactory.getCounter("thecounter"));
    assertEquals(0, inMemoryMetricsFactory.getCounter(Metrics.addTagsToMetricName("thecounter", tags)));
  }

  @Test
  public void counterValueIsIncreased() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    Counter counter = inMemoryMetricsFactory.createCounter("thecounter", tags);
    assertEquals(0, inMemoryMetricsFactory.getCounter(Metrics.addTagsToMetricName("thecounter", tags)));

    counter.inc(1);

    assertEquals(1, inMemoryMetricsFactory.getCounter(Metrics.addTagsToMetricName("thecounter", tags)));
  }

  @Test
  public void metricNameIsUsedForTimer() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    inMemoryMetricsFactory.createTimer("thetimer", tags);

    assertEquals(-1, inMemoryMetricsFactory.getTimer("thetimer"));
    assertEquals(0, inMemoryMetricsFactory.getTimer(Metrics.addTagsToMetricName("thetimer", tags)));
  }

  @Test
  public void timerValueIsIncreased() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    Timer timer = inMemoryMetricsFactory.createTimer("thetimer", tags);
    assertEquals(0, inMemoryMetricsFactory.getTimer(Metrics.addTagsToMetricName("thetimer", tags)));

    timer.durationMicros(1);

    assertEquals(1, inMemoryMetricsFactory.getTimer(Metrics.addTagsToMetricName("thetimer", tags)));
  }

  @Test
  public void metricNameIsUsedForGauge() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    inMemoryMetricsFactory.createGauge("thegauge", tags);

    assertEquals(-1, inMemoryMetricsFactory.getGauge("thegauge"));
    assertEquals(0, inMemoryMetricsFactory.getGauge(Metrics.addTagsToMetricName("thegauge", tags)));
  }

  @Test
  public void gaugeValueIsIncreased() {
    Map<String, String> tags = new HashMap<>(1);
    tags.put("foo", "bar");

    InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
    Gauge gauge = inMemoryMetricsFactory.createGauge("thegauge", tags);
    assertEquals(0, inMemoryMetricsFactory.getGauge(Metrics.addTagsToMetricName("thegauge", tags)));

    gauge.update(1);

    assertEquals(1, inMemoryMetricsFactory.getGauge(Metrics.addTagsToMetricName("thegauge", tags)));
  }

  @Test
  public void canBeUsedWithMetrics() {
    InMemoryMetricsFactory metricsFactory = new InMemoryMetricsFactory();
    Tracer tracer =
        new Tracer.Builder("metricsFactoryTest", new InMemoryReporter(), new ConstSampler(true))
            .withMetrics(new Metrics(metricsFactory))
            .build();

    tracer.buildSpan("theoperation").start();
    assertEquals(1, metricsFactory.getCounter("jaeger:started_spans.sampled=y"));
    assertEquals(0, metricsFactory.getCounter("jaeger:started_spans.sampled=n"));
    assertEquals(1, metricsFactory.getCounter("jaeger:traces.sampled=y.state=started"));
    assertEquals(0, metricsFactory.getCounter("jaeger:traces.sampled=n.state=started"));
  }
}
