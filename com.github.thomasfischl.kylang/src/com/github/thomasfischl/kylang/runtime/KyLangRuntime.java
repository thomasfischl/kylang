package com.github.thomasfischl.kylang.runtime;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.script.ScriptException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.parser.ParseException;

import com.github.thomasfischl.kylang.test.TestLangStandaloneSetupGenerated;
import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.test.testLang.Model;
import com.github.thomasfischl.kylang.test.testLang.TestLangFactory;
import com.github.thomasfischl.kylang.util.TestLangModelUtils;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class KyLangRuntime {

  // @Inject
  // private Provider<ResourceSet> resourceSetProvider;

  // @Inject
  // private IResourceValidator validator;

  @Inject
  private IParser parser;

  private KyLangReporter reporter;

  private Map<String, KeywordDecl> definedKeywords = new HashMap<>();

  private Stack<Scope> stack = new Stack<>();

  protected KyLangRuntime() {
    reporter = new KyLangReporter();
  }

  public void parse(Reader reader) throws ScriptException {
    IParseResult result = parser.parse(reader);
    if (result.hasSyntaxErrors()) {
      throw new ParseException("Provided input contains syntax errors.");
    }

    if (result.getRootASTElement() instanceof Model) {
      System.out.println("Successful loaded model.");
      Model model = (Model) result.getRootASTElement();

      executeTestcase(model);

    } else {
      throw new ScriptException("Could not parse script.");
    }

    // List<Issue> list = validator.validate(result.getRootASTElement().eResource(), CheckMode.ALL, CancelIndicator.NullImpl);
    // if (!list.isEmpty()) {
    // for (Issue issue : list) {
    // System.err.println(issue);
    // }
    // return;
    // }

  }

  private void executeTestcase(Model model) {
    EList<KeywordDecl> keywords = model.getKeywords();
    loadDefinedKeywords(model);

    for (KeywordDecl keyword : keywords) {
      if (TestLangModelUtils.isTestcaseKeyword(keyword)) {
        System.out.println("----------------------------------------------------------------");
        System.out.println("Execute Testcase '" + keyword.getName() + "'");
        System.out.println("----------------------------------------------------------------");
        execute(keyword.getName());
        System.out.println("----------------------------------------------------------------");
      }
    }
  }

  private void execute(String keywordName) {
    stack.clear();

    // create initial scope
    KeywordCall keywordCall = TestLangFactory.eINSTANCE.createKeywordCall();
    keywordCall.setName(keywordName);
    stack.push(new Scope(null, Lists.newArrayList(keywordCall)));

    Object currKeyword = null;
    Scope currScope = null;
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
  }

  private void executeKeywordDeclEnd(KeywordDecl keyword, Scope currScope) {
    reporter.reportKeywordEnd(keyword.getName());
  }

  private void executeKeywordDeclBegin(KeywordDecl keyword, Scope currScope) {
    reporter.reportKeywordBegin(keyword.getName());

    if (TestLangModelUtils.isScriptedKeyword(keyword)) {
      // Execute a scripted keyword
      // TODO implement me
      throw new NotImplemented();
    } else {
      // Execute a container keyword
      if (keyword.getKeywordlist() != null) {
        stack.push(new Scope(currScope, keyword.getKeywordlist().getChildren()));
      }
    }
  }

  private void executeKeywordCall(KeywordCall keyword, Scope currScope) {
    if (TestLangModelUtils.isInlineKeyword(keyword)) {
      // Execute the keyword as inline keyword
      // TODO implement me
      throw new NotImplemented();
    } else {
      KeywordDecl keywordImpl = getKeywordDefinition(keyword.getName());

      if (keywordImpl != null) {
        stack.push(new Scope(currScope, keywordImpl));
      } else {
        reporter.reportUnkownKeyword(keyword.getName());
      }
    }
  }

  private Map<String, KeywordDecl> loadDefinedKeywords(Model model) {
    EList<KeywordDecl> keywords = model.getKeywords();
    definedKeywords.clear();
    for (KeywordDecl keyword : keywords) {
      definedKeywords.put(TestLangModelUtils.normalizeKeyword(keyword.getName()), keyword);
    }
    return definedKeywords;
  }

  private KeywordDecl getKeywordDefinition(String keywordName) {
    return definedKeywords.get(TestLangModelUtils.normalizeKeyword(keywordName));
  }

  public static KyLangRuntime createRuntime() {
    Injector injector = new TestLangStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
    return injector.getInstance(KyLangRuntime.class);
  }

  // ------------------------------------------------------------------------
  // inline class
  // ------------------------------------------------------------------------

}

class Scope {
  private int curpos = 0;
  private final KeywordDecl keyword;
  private final List<KeywordCall> list;
  private final Scope parent;

  public Scope(Scope parent, KeywordDecl keyword) {
    super();
    this.parent = parent;
    this.keyword = keyword;
    list = null;
  }

  public Scope(Scope parent, List<KeywordCall> list) {
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
