package com.github.thomasfischl.kylang.runtime.keywords.impl;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;
import com.github.thomasfischl.kylang.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.kylang.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.kylang.runtime.keywords.KeywordResult;

public class TestKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, KyLangReporter reporter) {
    reporter.reportKeywordBegin(keyword + "(scripted)");

    for (String name : scope.getVariableNames()) {
      reporter.log("Property: " + name + " -> " + scope.getVariable(name));
    }

    reporter.log("It works!!!");
    reporter.reportKeywordEnd(keyword + "(scripted)");
    return new KeywordResult(true);
  }
}
