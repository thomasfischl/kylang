package com.github.thomasfischl.kylang.runtime;

import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class KyLangScriptEngine extends AbstractScriptEngine {

  private ScriptEngineFactory factory;

  public KyLangScriptEngine(ScriptEngineFactory factory) {
    this.factory = factory;
  }

  @Override
  public Object eval(String script, ScriptContext context) throws ScriptException {
    KyLangRuntime runtime = KyLangRuntime.createRuntime();
    runtime.parse(new StringReader(script));
    return null;
  }

  @Override
  public Object eval(Reader reader, ScriptContext context) throws ScriptException {
    throw new IllegalStateException("Not implemented");
  }

  @Override
  public Bindings createBindings() {
    throw new IllegalStateException("Not implemented");
  }

  @Override
  public ScriptEngineFactory getFactory() {
    return factory;
  }

}
