package com.asgab.entity;

public class ProgressBar {
  private int value;
  private String barClass;
  private String bgClass;
  private String labelClass;

  private int[] valueArray = {0, 10, 30, 50, 70, 90, 100};
  private String[] barClassArray = {"progress-bar-danger", "progress-bar-yellow", "progress-bar-primary", "progress-bar-primary",
      "progress-bar-primary", "progress-bar-primary", "progress-bar-success"};
  private String[] bgClassArray = {"bg-red", "bg-yellow", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-green"};
  private String[] labelClassArray =
      {"label-danger", "label-warning", "label-primary", "label-primary", "label-primary", "label-primary", "label-success"};

  public ProgressBar() {

  }

  public ProgressBar(int value) {
    setValue(value);
  }

  public void setValue(int value) {
    this.value = value;
    for (int i = valueArray.length - 1; i >= 0; i--) {
      if (value >= valueArray[i]) {
        barClass = barClassArray[i];
        bgClass = bgClassArray[i];
        labelClass = labelClassArray[i];
        break;
      }
    }
  }

  public int getValue() {
    return value;
  }

  public String getBarClass() {
    return barClass;
  }

  public String getBgClass() {
    return bgClass;
  }

  public String getLabelClass() {
    return labelClass;
  }

  public String[] getBarClassArray() {
    return barClassArray;
  }

  public String[] getBgClassArray() {
    return bgClassArray;
  }

  public int[] getValueArray() {
    return valueArray;
  }

  public String[] getLabelClassArray() {
    return labelClassArray;
  }

  public void setLabelClassArray(String[] labelClassArray) {
    this.labelClassArray = labelClassArray;
  }

  public void setBarClass(String barClass) {
    this.barClass = barClass;
  }

  public void setBgClass(String bgClass) {
    this.bgClass = bgClass;
  }

  public void setLabelClass(String labelClass) {
    this.labelClass = labelClass;
  }

  public void setValueArray(int[] valueArray) {
    this.valueArray = valueArray;
  }

  public void setBarClassArray(String[] barClassArray) {
    this.barClassArray = barClassArray;
  }

  public void setBgClassArray(String[] bgClassArray) {
    this.bgClassArray = bgClassArray;
  }

}
