package com.github.thomasfischl.kylang.runtime.keywords;

public class KeywordResult {

  private final boolean success;
  
  public KeywordResult(boolean success) {
    this.success = success;
  }
  
  public boolean isSuccess() {
    return success;
  }
  
}
