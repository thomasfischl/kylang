package com.github.thomasfischl.kylang.runtime;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.Test;

public class ScriptedKeywordTest {

  @Test
  public void scriptKeywordTest() throws ScriptException {
    KyLangScriptEngineFactory factory = new KyLangScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("scriptKeywordTest.kytest")));
  }

}
