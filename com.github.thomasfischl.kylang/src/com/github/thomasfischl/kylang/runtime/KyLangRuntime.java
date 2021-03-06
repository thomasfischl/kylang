package com.github.thomasfischl.kylang.runtime;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.parser.ParseException;

import com.github.thomasfischl.kylang.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.kylang.runtime.keywords.KeywordResult;
import com.github.thomasfischl.kylang.test.TestLangStandaloneSetupGenerated;
import com.github.thomasfischl.kylang.test.testLang.Expr;
import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.test.testLang.KeywordMetatype;
import com.github.thomasfischl.kylang.test.testLang.Model;
import com.github.thomasfischl.kylang.test.testLang.PropertyDecl;
import com.github.thomasfischl.kylang.test.testLang.ScriptType;
import com.github.thomasfischl.kylang.test.testLang.TestLangFactory;
import com.github.thomasfischl.kylang.util.TestLangModelUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class KyLangRuntime {

  @Inject
  private IParser parser;

  private KyLangReporter reporter;

  private final Map<String, KeywordDecl> definedKeywords = new HashMap<>();

  private final Stack<KyLangScriptScope> stack = new Stack<>();

  protected KyLangRuntime() {
    reporter = new KyLangReporter();
  }

  public void setReporter(KyLangReporter reporter) {
    this.reporter = reporter;
  }

  public void loadLibrary(Reader reader) {
    IParseResult result = parser.parse(reader);
    if (result.hasSyntaxErrors()) {
      for (INode error : result.getSyntaxErrors()) {
        reporter.error(error.getSyntaxErrorMessage().toString());
      }
      throw new ParseException("Provided input contains syntax errors.");
    }

    if (result.getRootASTElement() instanceof Model) {
      reporter.log("Successful loaded model.");
      Model model = (Model) result.getRootASTElement();

      EList<KeywordDecl> keywords = model.getKeywords();
      reporter.log("Loading " + keywords.size() + " keywords from library");
      List<String> keywordNames = new ArrayList<>();
      for (KeywordDecl keyword : keywords) {
        keywordNames.add("\"" + TestLangModelUtils.normalizeKeyword(keyword.getName()) + "\"");
        definedKeywords.put(TestLangModelUtils.normalizeKeyword(keyword.getName()), keyword);
      }
      reporter.log("Loaded Keywords (" + Joiner.on(",").join(keywordNames) + ")");
    }
  }

  public void executeAllTestcases() {

    int successTests = 0;
    int failedTests = 0;
    int errorTests = 0;

    for (KeywordDecl keyword : definedKeywords.values()) {
      if (TestLangModelUtils.isTestcaseKeyword(keyword)) {
        try {
          reporter.log("----------------------------------------------------------------");
          reporter.log("Execute Testcase '" + keyword.getName() + "'");
          reporter.log("----------------------------------------------------------------");
          executeTestCase(keyword.getName());
          successTests++;
        } catch (KyLangScriptException e) {
          e.printStackTrace();
          errorTests++;
          reporter.error(e.getMessage());
        } catch (KyLangScriptFailedException e) {
          e.printStackTrace();
          failedTests++;
          reporter.error(e.getMessage());
        } catch (Exception e) {
          e.printStackTrace();
          errorTests++;
          reporter.error(e.getMessage());
        } finally {
          reporter.log("----------------------------------------------------------------");
        }
      }
    }

    reporter.log("  Results:");
    reporter.log("  Success Tests     : " + successTests);
    reporter.log("  Failed Tests      : " + failedTests);
    reporter.log("  Fatal Failed Tests: " + errorTests);
    reporter.log("----------------------------------------------------------------");

    if (errorTests > 0) {
      throw new IllegalStateException("Fatal failed tests!");
    }
  }

  private void executeTestCase(String keywordName) {
    stack.clear();

    try {
      reporter.reportTestCaseStart(keywordName);
      // create initial scope
      KeywordCall keywordCall = TestLangFactory.eINSTANCE.createKeywordCall();
      keywordCall.setName(keywordName);
      stack.push(new KyLangScriptScope(null, Lists.newArrayList(keywordCall)));

      Object currKeyword = null;
      KyLangScriptScope currScope = null;
      while (!stack.isEmpty()) {

        currScope = stack.peek();
        currKeyword = currScope.getNextOpt();
        if (currKeyword == null) {
          stack.pop();
          continue;
        }

        if (currKeyword instanceof KeywordCall) {
          KeywordCall keyword = (KeywordCall) currKeyword;
          executeKeywordCall(keyword, currScope);
        }

        if (currKeyword instanceof KeywordDecl) {
          KeywordDecl keyword = (KeywordDecl) currKeyword;

          if (currScope.isKeywordBegin()) {
            executeKeywordDeclBegin(keyword, currScope);
          } else if (currScope.isKeywordEnd()) {
            executeKeywordDeclEnd(keyword, currScope);
          } else {
            // TODO invalid state
            throw new IllegalStateException("The runtime has an invalid state.");
          }
        }
      }
    } finally {
      reporter.reportTestCaseEnd(keywordName);
    }
  }

  private void executeKeywordDeclEnd(KeywordDecl keyword, KyLangScriptScope currScope) {
    reporter.reportKeywordEnd(keyword.getName());
  }

  private void executeKeywordDeclBegin(KeywordDecl keyword, KyLangScriptScope currScope) {
    reporter.reportKeywordBegin(keyword.getName());

    if (TestLangModelUtils.isScriptedKeyword(keyword)) {
      executeScriptedKeyword(currScope, keyword);
    } else {
      // Execute a container keyword
      if (keyword.getKeywordlist() != null) {
        stack.push(new KyLangScriptScope(currScope, keyword.getKeywordlist().getChildren()));
      }
    }
  }

  private void executeScriptedKeyword(KyLangScriptScope currScope, KeywordDecl keyword) {
    if (keyword.getScript() == null) {
      throw new KyLangScriptException("Keyword '" + keyword.getName() + "': Missing script configuraiton.");
    }
    if (ScriptType.JAVA == keyword.getScript().getScriptType()) {
      String keywordClass = keyword.getScript().getClass_();
      try {
        Class<?> clazz = getClass().getClassLoader().loadClass(keywordClass);
        if (IScriptedKeyword.class.isAssignableFrom(clazz)) {
          IScriptedKeyword scripedKeyword = (IScriptedKeyword) clazz.newInstance();

          KeywordResult result = scripedKeyword.execute(keyword.getName(), currScope, reporter);
          if (!result.isSuccess()) {
            // TODO improve this implementation
            throw new KyLangScriptFailedException("Failed Keyword");
          }
        } else {
          throw new KyLangScriptException("Class '" + clazz.getSimpleName() + "' does not implement the IScriptedKeyword.");
        }
      } catch (ClassNotFoundException e) {
        reporter.error(e);
        throw new KyLangScriptException("Keyword class '" + keywordClass + "' can't be found.");
      } catch (InstantiationException | IllegalAccessException e) {
        reporter.error(e);
        throw new KyLangScriptException("An error occurs during creating the scripted keyword object. Message: " + e.getMessage());
      }
    } else {
      throw new KyLangScriptException("Unkown script keyword type '" + keyword.getScript().getScriptType() + "'.");
    }
  }

  private void executeKeywordCall(KeywordCall keyword, KyLangScriptScope currScope) {
    if (TestLangModelUtils.isInlineKeyword(keyword)) {
      // Execute the keyword as inline keyword
      // Create a temporary keyword implementation for the inline keyword and push it on the execution stack
      KeywordDecl keywordImpl = TestLangFactory.eINSTANCE.createKeywordDecl();
      keywordImpl.setName(keyword.getName());
      keywordImpl.setMetatype(KeywordMetatype.USERDEFINED);
      keywordImpl.setKeywordlist(keyword.getKeywordList().getKeywordlist());
      stack.push(new KyLangScriptScope(currScope, keywordImpl));
    } else {
      KeywordDecl keywordImpl = getKeywordDefinition(keyword.getName());
      if (keywordImpl != null) {
        KyLangScriptScope scope = createCallScope(keyword, currScope, keywordImpl);
        stack.push(scope);
      } else {
        reporter.reportUnkownKeyword(keyword.getName());
      }
    }
  }

  private KyLangScriptScope createCallScope(KeywordCall keyword, KyLangScriptScope currScope, KeywordDecl keywordImpl) {
    KyLangScriptScope scope = new KyLangScriptScope(currScope, keywordImpl);

    EList<Expr> callParameters = new BasicEList<>();
    EList<PropertyDecl> properties = new BasicEList<>();

    if (keyword.getParameters() != null) {
      callParameters = keyword.getParameters().getParameters();
    }

    if (keywordImpl.getProperties() != null) {
      properties = keywordImpl.getProperties();
    }

    if (callParameters.size() == properties.size()) {

      KyLangExpressionEvaluator exprEval = new KyLangExpressionEvaluator(currScope);

      // TODO check parameter type (in, out, inout)
      for (int i = 0; i < callParameters.size(); i++) {
        scope.addVariable(properties.get(i).getName(), exprEval.eval(callParameters.get(i)));
      }

    } else {
      throw new KyLangScriptException("Invalid number of arguments for keyword '" + keyword.getName() + "'.");
    }

    return scope;
  }

  private KeywordDecl getKeywordDefinition(String keywordName) {
    return definedKeywords.get(TestLangModelUtils.normalizeKeyword(keywordName));
  }

  public static KyLangRuntime createRuntime() {
    Injector injector = new TestLangStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
    return injector.getInstance(KyLangRuntime.class);
  }
}
