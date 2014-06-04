package com.github.thomasfischl.kylang.runtime;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.Test;

public class KyLangScriptEngineTest {

  @Test
  public void simpleStringTest() throws ScriptException {

    String source = "";

    source += "keyword<testcase> HelloWorld { \n  Keyword 1                  \n Keyword 2 \n } \n";
    source += "keyword           Keyword 1  { \n  Login with default account              \n } \n";
    source += "keyword           Keyword 2  { \n  Action1                                 \n } \n";

    KyLangScriptEngineFactory factory = new KyLangScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(source);
  }

  @Test
  public void simpleFileTest() throws ScriptException {
    KyLangScriptEngineFactory factory = new KyLangScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("simple-test.kytest")));
  }

}
