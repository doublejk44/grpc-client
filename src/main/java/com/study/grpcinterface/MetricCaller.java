package com.study.grpcinterface;

import com.nkia.ias.MetricRequest;
import com.nkia.ias.MetricResponse;
import com.nkia.ias.MetricServiceGrpc;
import com.nkia.ias.MetricServiceGrpc.MetricServiceBlockingStub;
import com.nkia.ias.MetricServiceGrpc.MetricServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MetricCaller {
  private ManagedChannel channel;
  private MetricServiceStub metricServiceStub;
  private MetricServiceBlockingStub metricServiceBlockingStub;

  public MetricCaller(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();
    metricServiceStub = MetricServiceGrpc.newStub(channel);
    metricServiceBlockingStub = MetricServiceGrpc.newBlockingStub(channel);
  }

  public void sendBlockingUnary(List<MetricRequest> metrics) {
    for (MetricRequest metric : metrics) {
      metricServiceBlockingStub.metricHandler(metric);
    }
  }

  public void sendAsynClientStream(List<MetricRequest> metrics) {
    StreamObserver<MetricResponse> responseObserver = new StreamObserver<MetricResponse>() {
      @Override
      public void onNext(MetricResponse value) {
//        System.out.println("결과: " + value.getMessage());
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("error");
      }

      @Override
      public void onCompleted() {
        System.out.println("Async 완료. time: " + System.currentTimeMillis());
      }
    };

    StreamObserver<MetricRequest> requestObserver = metricServiceStub.metricStreamHandler(responseObserver);
    for(MetricRequest request : metrics) {
      requestObserver.onNext(request);
    }
    requestObserver.onCompleted();
  }

  public void sendByRest(List<Metric> metrics) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/metric";
    HttpEntity<List<Metric>> request = new HttpEntity<>(metrics);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    System.out.println(responseEntity.getBody().toString());
  }

}
