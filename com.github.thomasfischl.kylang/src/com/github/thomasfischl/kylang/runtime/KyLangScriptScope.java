package com.github.thomasfischl.kylang.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.thomasfischl.kylang.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;

class KyLangScriptScope implements IKeywordScope {
  private int curpos = 0;
  private final KeywordDecl keyword;
  private final List<KeywordCall> list;
  private final KyLangScriptScope parent;
  private final Map<String, Object> variables = new HashMap<>();

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

  @Override
  public void addVariable(String name, Object value) {
    variables.put(name, value);
  }

  @Override
  public Object getVariable(String name) {
    if (!variables.containsKey(name)) {
      throw new KyLangScriptException("Variable '" + name + "' is not declared.");
    }
    return variables.get(name);
  }

  @Override
  public boolean getVariableAsBoolean(String name) {
    Object value = getVariable(name);
    if (value instanceof Boolean) {
      return (boolean) value;
    } else {
      throw new KyLangScriptException("Variable '" + name + "' is not from type 'boolean'.");
    }
  }

  @Override
  public Set<String> getVariableNames() {
    return variables.keySet();
  }
}