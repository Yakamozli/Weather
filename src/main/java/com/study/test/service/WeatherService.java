package com.study.test.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author Mason
 * @title: WeatherService
 * @description: TODO
 * @date 2022/1/19 21:13
 */
@Service
public class WeatherService {
  private static final String CHINA_URL_PROVINCE =
      "http://www.weather.com.cn/data/city3jdata/china.html";
  private static final String CHINA_URL_CITY = "http://www.weather.com.cn/data/city3jdata/provshi/";
  private static final String CHINA_URL_COUNTY =
      "http://www.weather.com.cn/data/city3jdata/station/";
  private static final String CHINA_URL_CODE = "http://www.weather.com.cn/data/sk/";
  private static final String SUFFIX = ".html";

  /**
   * 传入省市区获得温度
   *
   * @param province
   * @param city
   * @param county
   * @return
   */
  public String getTemperature(String province, String city, String county) {
    if (Objects.isNull(province)) {
      return "您输入的省为空～请重新输入";
    }
    if (Objects.isNull(city)) {
      return "您输入的市为空～请重新输入";
    }
    if (Objects.isNull(county)) {
      return "您输入的县为空～请重新输入";
    }
    int code = getCode(province, city, county);
    if (code == -1) {
      return "您输入的问题地址有误～请重新输入";
    } else {
      return "当前温度为:" + getTemp(CHINA_URL_CODE + code + SUFFIX) + "℃";
    }
  }

  /**
   * 传入省市区获得省市区code
   *
   * @param province
   * @param city
   * @param county
   * @return
   */
  public int getCode(String province, String city, String county) {
    String code;
    String result = HttpUtil.get(CHINA_URL_PROVINCE);
    JSONObject jsonObject = JSONUtil.parseObj(result);
    Iterator sIterator = jsonObject.keySet().iterator();
    while (sIterator.hasNext()) {
      // 获得key
      String key = (String) sIterator.next();
      // 获得key值对应的value
      String value = jsonObject.getStr(key);
      if (province.contains(value)) {
        // value 省的代码
        code = key;
        result = HttpUtil.get(CHINA_URL_CITY + code + SUFFIX);
        jsonObject = JSONUtil.parseObj(result);
        sIterator = jsonObject.keySet().iterator();
        while (sIterator.hasNext()) {
          key = (String) sIterator.next();
          value = jsonObject.getStr(key);
          if (city.contains(value)) {
            code = code + key;
            // value 市的代码
            result = HttpUtil.get(CHINA_URL_COUNTY + code + SUFFIX);
            jsonObject = JSONUtil.parseObj(result);
            sIterator = jsonObject.keySet().iterator();
            while (sIterator.hasNext()) {
              key = (String) sIterator.next();
              value = jsonObject.getStr(key);
              if (county.contains(value)) {
                code = code + key;
                // value 县的代码
                return new Integer(code);
              }
            }
          }
        }
      }
    }
    return -1;
  }

  /**
   * 通过url获取温度
   *
   * @param url
   * @return
   */
  public Object getTemp(String url) {
    String result = HttpUtil.get(url);
    JSONObject jsonObject = JSONUtil.parseObj(result);
    Object weatherinfo = jsonObject.get("weatherinfo");
    jsonObject = JSONUtil.parseObj(weatherinfo);
    Object temp = jsonObject.get("temp");
    return temp;
  }
}
