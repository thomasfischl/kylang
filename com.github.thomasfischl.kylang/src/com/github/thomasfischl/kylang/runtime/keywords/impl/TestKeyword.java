package com.github.thomasfischl.kylang.runtime.keywords.impl;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;
import com.github.thomasfischl.kylang.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.kylang.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.kylang.runtime.keywords.KeywordResult;

public class TestKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(IKeywordScope scope, KyLangReporter reporter) {
    
    for(String name : scope.getVariableNames()){
      reporter.log("Property: " + name + " -> " + scope.getVariable(name));
    }
    
    
    reporter.log("It works!!!");
    return new KeywordResult(true);
  }

}
