package com.asgab.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Servlets {

  public static String encodeParameterString(Map<String, Object> params) {
    if (Collections3.isEmpty(params)) {
      return "";
    }

    StringBuilder queryStringBuilder = new StringBuilder();
    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, Object> entry = it.next();
      if ("sort".equals(entry.getKey()))
        continue;
      if (entry.getValue().getClass().isArray()) {
        String[] arr = (String[]) entry.getValue();
        if (arr != null && arr.length > 0) {
          for (int i = 0; i < arr.length; i++) {
            queryStringBuilder.append(entry.getKey()).append('=').append(arr[i]);
            if (i < arr.length - 1) {
              queryStringBuilder.append('&');
            }
          }
        }
      } else {
        queryStringBuilder.append(entry.getKey()).append('=').append(entry.getValue());
      }
      if (it.hasNext()) {
        queryStringBuilder.append('&');
      }
    }
    return queryStringBuilder.toString();
  }
}
