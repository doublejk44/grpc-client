package com.study.grpcinterface;

import com.nkia.ias.MetricRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class MetricHandler implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    MetricCaller caller = new MetricCaller("localhost", 8081);

    List<MetricRequest> metrics = new ArrayList<>();
    for(int i = 0; i < 100000; i++) {
      metrics.add(MetricRequest.newBuilder()
          .setMetricId(i)
          .setMetricName("Name" + i)
          .setMetricType("Type")
          .setMetricUnit("Unit")
          .setResourceId(i)
          .setTime(51561561566L)
          .setValue(45646545d)
          .build());
    }

    StopWatch stopWatch = new StopWatch();

    stopWatch.start("Rest");
    List<Metric> metrics2 = new ArrayList<>();
    for(long i = 0; i < 100000; i++) {
      metrics2.add(Metric.builder()
          .metricId(i)
          .metricName("Name" + i)
          .metricType("Type")
          .metricUnit("Unit")
          .resourceId(i)
          .time(51561561566L)
          .value(45646545d)
          .build());
    }
    caller.sendByRest(metrics2);
    stopWatch.stop();

    stopWatch.start("Grpc Async");
    caller.sendAsynClientStream(metrics);
    stopWatch.stop();

//    stopWatch.start("Grpc Unary");
//    caller.sendBlockingUnary(metrics);
//    stopWatch.stop();

    System.out.println(stopWatch.prettyPrint());
    System.out.println("Client end");
  }

}
