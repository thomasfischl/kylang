package com.github.thomasfischl.kylang.runtime.keywords;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;

public interface IScriptedKeyword {

  KeywordResult execute(IKeywordScope scope, KyLangReporter reporter);

}
