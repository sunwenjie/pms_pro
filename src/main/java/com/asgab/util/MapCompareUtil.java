package com.asgab.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class MapCompareUtil {

  private static JsonMapper jsonMapper = new JsonMapper();

  private static PropertiesLoader compare_info_properties = new PropertiesLoader("log_compare_keys.properties");

  private static PropertiesLoader message_zh_properties = new PropertiesLoader("message_zh_CN.properties");

  private static PropertiesLoader message_en_properties = new PropertiesLoader("message_en_US.properties");

  // 空格字符_zh
  public static final String SPACE_CHAR_ZH = "空";

  // 空格字符_en
  public static final String SPACE_CHAR_EN = "Empty";

  public static String getProperty(String language, String key) {
    return "zh".equalsIgnoreCase(language) ? message_zh_properties.getProperty(key) : message_en_properties.getProperty(key);
  }

  @SuppressWarnings("unchecked")
  public static String compareToDifferent(Map beforeMap, Map afterMap, String compareKeys, String language) {
    Properties properties = compare_info_properties.getProperties();
    if (beforeMap == null || afterMap == null || properties == null)
      return "";

    final StringBuffer rs = new StringBuffer();

    Map<String, String> compareInfo = jsonMapper.fromJson(properties.get(compareKeys).toString(), Map.class);

    for (String key : compareInfo.keySet()) {
      String label = getProperty(language, compareInfo.get(key));
      Object v1 = beforeMap.get(key);
      Object v2 = afterMap.get(key);

      if (v1 == null && v2 != null) {
        rs.append(label + " : " + getEmpty(language) + " → " +  transformToStr(v2, key) + "; ");
      } else if (v1 != null && v2 == null) {
        rs.append(label + " : " + transformToStr(v1, key) + " → " + getEmpty(language)+ "; ");
      } else if (v1 != null && v2 != null && !transformToStr(v1, key).equals(transformToStr(v2, key))) {
        rs.append(label + " : " + filterEmpty(transformToStr(v1, key), language) + " → " + filterEmpty(transformToStr(v2, key), language) + "; ");
      }
    }

    return rs.toString();
  }

  private static String filterEmpty(String source, String language) {
    if (source == null || "".equals(source))
      return getEmpty(language);
    return source;
  }

  private static String getEmpty(String language) {
    return "zh".equalsIgnoreCase(language) ? SPACE_CHAR_ZH : SPACE_CHAR_EN;
  }

  public static String transformToStr(Object obj) {
    return transformToStr(obj, "");
  }

  public static String transformToStr(Object obj, String key) {
    if (obj == null)
      return "";
    if (obj instanceof Date)
      return new SimpleDateFormat("yyyy-MM-dd").format(obj);
    if (obj instanceof String)
      return obj.toString();
    if (obj instanceof Number) {
      if (obj instanceof Long && key.contains("date")) {
        Date date = new Date(((Number) obj).longValue());
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
      }
      return String.valueOf(obj);
    }

    return "";
  }

}
