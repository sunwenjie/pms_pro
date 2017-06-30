package com.asgab.util;

public enum Operate {

  ADD("新建", "Add", 1), MODIFY("修改", "Modify", 2), DELETE("删除", "Delete", 3), UPLOAD("上传", "Upload", 4), REVIEW("合并", "Review", 5);

  private String name;
  private String nameEN;
  private int index;

  private Operate(String name, String nameEN, int index) {
    this.name = name;
    this.nameEN = nameEN;
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }

  public String getName() {
    return this.name;
  }

  public String getNameEN() {
    return this.nameEN;
  }

  public String toString() {
    return this.index + "_" + this.name;
  }

  public static Operate decodeIndex(final int index) {
    Operate result = null;
    Operate[] operates = Operate.values();
    for (Operate operate : operates) {
      if (index == operate.getIndex()) {
        result = operate;
        break;
      }
    }

    return result;
  }

}
