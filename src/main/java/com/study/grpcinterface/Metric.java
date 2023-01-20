package com.study.grpcinterface;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metric implements Serializable {
  private Long resourceId;
  private Long metricId;
  private String metricName;
  private String metricType;
  private String metricUnit;
  private Long time;
  private Double value;
}
