package com.github.thomasfischl.kylang.runtime;

public class KyLangExpressionSymbol {

  private final String name;

  public KyLangExpressionSymbol(String name) {
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
