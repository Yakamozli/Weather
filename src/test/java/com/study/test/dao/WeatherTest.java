package com.study.test.dao;

import com.study.test.service.WeatherService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeatherTest {

  @Autowired private WeatherService weatherService;

  // 引入 ContiPerf 进行性能测试
  @Rule public ContiPerfRule contiPerfRule = new ContiPerfRule();

  @Test
  // 10个线程 执行1000次
  @PerfTest(invocations = 1000, threads = 10)
  public void test() {
    System.out.println(weatherService.getTemperature("江苏省", "苏州市", "吴中区"));
  }
}
