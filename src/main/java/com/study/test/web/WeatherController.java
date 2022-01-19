package com.study.test.web;

import com.study.test.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

  @Autowired private WeatherService weatherService;

  @GetMapping("/getTemperature")
  public String getTemperature(String province, String city, String county) {
    return weatherService.getTemperature(province, city, county);
  }
}
