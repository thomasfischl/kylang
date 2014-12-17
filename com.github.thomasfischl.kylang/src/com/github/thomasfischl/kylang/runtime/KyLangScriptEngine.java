package com.github.thomasfischl.kylang.runtime;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class KyLangScriptEngine extends AbstractScriptEngine {

  private ScriptEngineFactory factory;

  private KyLangReporter reporter;

  public KyLangScriptEngine(ScriptEngineFactory factory) {
    this.factory = factory;
  }

  public void setReporter(KyLangReporter reporter) {
    this.reporter = reporter;
  }

  @Override
  public Object eval(String script, ScriptContext context) throws ScriptException {
    return eval(new StringReader(script), context);
  }

  @Override
  public Object eval(Reader reader, ScriptContext context) throws ScriptException {
    KyLangRuntime runtime = KyLangRuntime.createRuntime();
    if (reader != null) {
      runtime.setReporter(reporter);
    }
    runtime.loadLibrary(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("control-structure.kytest")));
    runtime.loadLibrary(reader);
    runtime.executeAllTestcases();
    return null;
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
