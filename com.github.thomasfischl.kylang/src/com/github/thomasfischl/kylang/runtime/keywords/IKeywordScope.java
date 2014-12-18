package com.github.thomasfischl.kylang.runtime.keywords;

import java.util.Set;

public interface IKeywordScope {

  String getVariable(String name);

  Set<String> getVariableNames();

  void addVariable(String name, String value);

}
