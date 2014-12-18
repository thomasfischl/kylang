package com.github.thomasfischl.kylang.runtime.keywords.impl;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;
import com.github.thomasfischl.kylang.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.kylang.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.kylang.runtime.keywords.KeywordResult;

public class PrintKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, KyLangReporter reporter) {
    reporter.log(scope.getVariable("text").toString());
    return new KeywordResult(true);
  }
}
