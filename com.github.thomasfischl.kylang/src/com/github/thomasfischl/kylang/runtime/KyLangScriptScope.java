package com.github.thomasfischl.kylang.runtime;

import java.util.List;

import com.github.thomasfischl.kylang.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;

class KyLangScriptScope implements IKeywordScope{
  private int curpos = 0;
  private final KeywordDecl keyword;
  private final List<KeywordCall> list;
  private final KyLangScriptScope parent;

  public KyLangScriptScope(KyLangScriptScope parent, KeywordDecl keyword) {
    super();
    this.parent = parent;
    this.keyword = keyword;
    list = null;
  }

  public KyLangScriptScope(KyLangScriptScope parent, List<KeywordCall> list) {
    super();
    this.parent = parent;
    this.list = list;
    keyword = null;
  }

  Object getNextOpt() {
    if (keyword != null && curpos < 2) {
      curpos++;
      return keyword;
    }
    if (list != null && curpos < list.size()) {
      return list.get(curpos++);
    }
    return null;
  }

  boolean isKeywordBegin() {
    if (keyword == null) {
      throw new IllegalStateException("This method is only allowd for a keyword scope");
    }

    return curpos == 1;
  }

  boolean isKeywordEnd() {
    if (keyword == null) {
      throw new IllegalStateException("This method is only allowd for a keyword scope");
    }
    return curpos == 2;
  }
}