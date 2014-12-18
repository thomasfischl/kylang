package com.github.thomasfischl.kylang.runtime;

public class KyLangExpressionLocator {

  private final String name;

  public KyLangExpressionLocator(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
