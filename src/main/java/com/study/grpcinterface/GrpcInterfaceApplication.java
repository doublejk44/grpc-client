package com.study.grpcinterface;

import com.nkia.ias.MetricRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

@SpringBootApplication
public class GrpcInterfaceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GrpcInterfaceApplication.class, args);

  }

}
