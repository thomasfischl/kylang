package com.github.thomasfischl.kylang.runtime;

public class KyLangReporter {

  private int indentation;

  public void reportKeywordBegin(String name) {
    report("--> begin keyword: " + name);
    indentation++;
  }

  public void reportKeywordEnd(String name) {
    indentation--;
    report("<-- end keyword: " + name);
  }

  public void reportUnkownKeyword(String name) {
    report("??? unkown keyword: " + name);
  }

  private void report(String msg) {
    System.out.println("                         ".substring(0, indentation * 2) + msg);
  }

}
