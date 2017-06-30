package com.asgab.entity;

public class Opportunity {
  private Long id;
  private String task;
  private int progress;
  private ProgressBar progressBar;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }

  public void setProgressBar(ProgressBar progressBar) {
    this.progressBar = progressBar;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

}
