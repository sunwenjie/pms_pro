package com.asgab.core.mail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class HelloWorldFreeMarkerStyle {

  public static void main(String[] args)
      throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {

    Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);

    configuration.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class, "/ftl/");



    Template helloTemp = configuration.getTemplate("my.ftl");
    StringWriter writer = new StringWriter();
    Map<String, Object> helloMap = new HashMap<String, Object>();
    helloMap.put("content", "gokhan");

    helloTemp.process(helloMap, writer);

    System.out.println(writer);


  }
}
