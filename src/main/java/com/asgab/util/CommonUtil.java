package com.asgab.util;

import com.alibaba.fastjson.JSONArray;
import com.asgab.entity.ExchangeRate;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

  private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

  public static final PropertiesLoader properties_zh = new PropertiesLoader("message_zh_CN.properties");
  public static final PropertiesLoader properties_en = new PropertiesLoader("message_en_US.properties");
  public static final PropertiesLoader jdbc = new PropertiesLoader("application.properties");


  public static final Map<String, String> LOG_MODULE_ZH = new TreeMap<String, String>();
  public static final Map<String, String> LOG_MODULE_EN = new TreeMap<String, String>();
  public static final Map<String, String> LOG_OPERATE_TYPE_ZH = new TreeMap<String, String>();
  public static final Map<String, String> LOG_OPERATE_TYPE_EN = new TreeMap<String, String>();


  public static String ip = "";

  static {
    ip = getAddress();
    for (Operate oper : Operate.values()) {
      LOG_OPERATE_TYPE_ZH.put(String.valueOf(oper.getIndex()), oper.getName());
      LOG_OPERATE_TYPE_EN.put(String.valueOf(oper.getIndex()), oper.getNameEN());
    }

  }



  private static String getAddress() {
    try {
      Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        Enumeration<InetAddress> ee = n.getInetAddresses();
        while (ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();

          if (i.isSiteLocalAddress()) {
            return i.getHostAddress();
          }

          // System.out.println(i.getHostAddress());
          // System.out.println(" isLoopbackAddress: "+i.isLoopbackAddress()
          // +" isLinkLocalAddress: "+i.isLinkLocalAddress()
          // +" isSiteLocalAddress: "+i.isSiteLocalAddress()
          // +" isAnyLocalAddress: " +i.isAnyLocalAddress()
          // +" isMulticastAddress: "+i.isMulticastAddress());
        }
      }
    } catch (SocketException e) {
      log.debug("Error when getting host ip address: <{}>.", e.getMessage());
    }
    return "";
  }



  public static String i18nStr(ServletRequest request, String zh, String en) {
    return "zh".equalsIgnoreCase(request.getLocale().getLanguage()) ? zh : en;
  }

  public static String formatDate(Date date) {
    return date != null ? formatDate(date, "yyyy-MM-dd HH:mm:ss") : "";
  }

  public static String formatDate(Date date, String format) {
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    } else {
      return "";
    }
  }


  /**
   * 保留2位小数
   * 
   * @param num
   * @return
   */
  public static String reserved2Digit(String num) {
    return reserved2Digit(Double.parseDouble(num));
  }

  public static String reserved2Digit(Double num) {
    String format = "0.00";
    return new DecimalFormat(format).format(num);
  }

  public static double format2Number(String num) {
    return format2Number(Double.parseDouble(num));
  }

  public static double format2Number(Double num) {
    return Double.valueOf(new DecimalFormat("#.##").format(num));
  }

  /**
   * 数字千位分割, 保留2位
   * 
   * @param num
   * @return
   */
  public static String digSeg(Double num) {
    String format = "#,##0.00";
    return new DecimalFormat(format).format(num);
  }

  /**
   * list去重复
   * 
   * @param list
   */
  public static void distinctList(List<String> list) {
    for (int i = list.size() - 1; i >= 0; i--) {
      boolean exist = false;
      for (int j = 0; j < i; j++) {
        if (list.get(i).equalsIgnoreCase(list.get(j))) {
          list.remove(i);
          exist = true;
          break;
        }
      }
      if (exist) {
        continue;
      }
    }
  }

  public static void distinctLongList(List<Long> list) {
    for (int i = list.size() - 1; i >= 0; i--) {
      boolean exist = false;
      for (int j = 0; j < i; j++) {
        if (list.get(i) != null && list.get(i).equals(list.get(j))) {
          list.remove(i);
          exist = true;
          break;
        }
      }
      if (exist) {
        continue;
      }
    }
  }

  /***
   * 获取国家化资源文件，按key获取
   * 
   * @param request
   * @param key
   * @return
   */
  public static String getProperty(HttpServletRequest request, String key) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    String lang = localeResolver.resolveLocale(request).getLanguage();
    String country = localeResolver.resolveLocale(request).getCountry();
    return getProperty(lang, country, key);
  }

  public static String getProperty(String lang, String country, String key) {
    if (properties_zh == null || properties_en == null || !StringUtils.isNotBlank(key)) {
      return "";
    }
    return ("zh".equalsIgnoreCase(lang) && "CN".equalsIgnoreCase(country)) ? properties_zh.getProperty(key) : properties_en.getProperty(key);
  }

  // 是否是真实环境
  public static boolean isProduct() {
    return "true".equalsIgnoreCase(jdbc.getProperty("jdbc.product"));
  }

  public static String array2String(String[] array) {
    if (array != null && array.length > 0) {
      JSONArray jsonArray = new JSONArray();
      for (String s : array) {
        jsonArray.add(s);
      }
      return jsonArray.toJSONString();
    }
    return "";
  }

  public static String[] string2Array(String str) {
    if (StringUtils.isNotBlank(str)) {
      JSONArray jsonArray = JSONArray.parseArray(str);
      String[] strArr = new String[jsonArray.size()];
      for (int i = 0; i < jsonArray.size(); i++) {
        strArr[i] = jsonArray.getString(i);
      }
      return strArr;
    }
    return new String[0];
  }

  public static BigDecimal transferMoneyToRMB(List<ExchangeRate> exchangeRates, String currency_name, Double money) {
    if ("RMB".equalsIgnoreCase(currency_name)) {
      return new BigDecimal(money);
    }
    for (int i = 0; i < exchangeRates.size(); i++) {
      // currency-CNY 乘法
      if (currency_name.equalsIgnoreCase(exchangeRates.get(i).getBase_currency()) && "RMB".equalsIgnoreCase(exchangeRates.get(i).getCurrency())) {
        return new BigDecimal(money).multiply(new BigDecimal(exchangeRates.get(i).getRate()));
      }
    }
    return BigDecimal.ZERO;
  }

  public static boolean containValue(List<Long> list, Long value) {
    boolean flag = false;
    if (list != null) {
      for (Long tmpVal : list) {
        if (tmpVal != null && tmpVal.equals(value)) {
          flag = true;
          break;
        }
      }
    }
    return flag;
  }

  public static boolean containValue(List<String> list, String value) {
    boolean flag = false;
    if (list != null) {
      for (String tmpVal : list) {
        if (tmpVal != null && tmpVal.equalsIgnoreCase(value)) {
          flag = true;
          break;
        }
      }
    }
    return flag;
  }

  /**
   * 时间格式分割问题. yyyy/MM/dd - yyyy/MM/dd 改为 yyyy-MM-dd - yyyy-MM-dd 反之
   * 
   * @param sourceSplit
   * @param targetSplit
   * @return
   */
  public static String changeDateFormat(String date, String sourceSplit, String targetSplit) {
    if (date != null && date.length() == 10) {
      return date.replaceAll(sourceSplit, targetSplit);
    }
    return "";
  }

  public static void main(String[] args) throws UnknownHostException {
    System.out.println(getAddress());
  }


  // 1来自DB，2来自条件
  public static int crossDate(String startDate1, String endDate1, String startDate2, String endDate2) {

    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

    DateTime s1 = DateTime.parse(startDate1, format);
    DateTime e1 = DateTime.parse(endDate1, format);
    e1 = e1.plusDays(1);
    DateTime s2 = DateTime.parse(startDate2, format);
    DateTime e2 = DateTime.parse(endDate2, format);
    e2 = e2.plusDays(1);


    Days days = Days.ZERO;
    // 错误的开始结束日期
    if (s1.isAfter(e1) || s2.isAfter(e2)) {
      return days.getDays();
    }

    if (s1.isBefore(s2) && e1.isBefore(e2) && (s2.isBefore(e1) || s2.isEqual(e1))) {
      days = Days.daysBetween(s2, e1);

    } else if (s1.isAfter(s2) && e1.isAfter(e2) && (s1.isBefore(e2) || s1.isEqual(e2))) {
      days = Days.daysBetween(s1, e2);

    } else if ((s1.isAfter(s2) || s1.isEqual(s2)) && (e1.isBefore(e2) || e1.isEqual(e2))) {
      days = Days.daysBetween(s1, e1);

    } else if ((s1.isBefore(s2) || s1.isEqual(s2)) && (e1.isAfter(e2) || e1.isEqual(e2))) {
      days = Days.daysBetween(s2, e2);
    }

    return days.getDays();
  }

  public static int gapDate(String date1, String date2) {
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

    DateTime start = DateTime.parse(date1, format);
    DateTime end = DateTime.parse(date2, format);
    end = end.plusDays(1);

    return Days.daysBetween(start, end).getDays();
  }

  /**
   * 获取失败通知管理员邮箱
   *
   * @return
   */
  public static List<String> getErrorAdminMails() {
    List<String> mails = new ArrayList<String>();
    try {
      PropertiesLoader appPropertie = new PropertiesLoader("mail/mail.properties");
      String mailStr = appPropertie.getProperty("error.admin");
      if (StringUtils.isNotBlank(mailStr)) {
        for (String mail : mailStr.split(",")) {
          mails.add(mail);
        }
      }
      if (mails.size() == 0) {
        mails.add("wenjie.sun@i-click.com");
        mails.add("jerry.li@i-click.com");
      }
    } catch (Exception ex) {
      mails.add("wenjie.sun@i-click.com");
      return mails;
    }
    return mails;
  }

}
