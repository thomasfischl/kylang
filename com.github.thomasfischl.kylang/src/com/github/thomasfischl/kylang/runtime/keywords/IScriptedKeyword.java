package com.github.thomasfischl.kylang.runtime.keywords;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;

public interface IScriptedKeyword {

  KeywordResult execute(String keyword, IKeywordScope scope, KyLangReporter reporter);

}
