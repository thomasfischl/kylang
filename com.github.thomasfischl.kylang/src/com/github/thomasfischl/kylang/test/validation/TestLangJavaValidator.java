package com.github.thomasfischl.kylang.test.validation;

import java.util.List;

import org.eclipse.xtext.validation.Check;

import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.test.testLang.TestLangPackage;
import com.github.thomasfischl.kylang.util.TestLangModelUtils;

/**
 * Custom validation rules.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
public class TestLangJavaValidator extends com.github.thomasfischl.kylang.test.validation.AbstractTestLangJavaValidator {

  public static final String KEYWORD_NOT_EXISTS = "KEYWORD_NOT_EXISTS";

  @Check
  public void checkKeywordCallExists(KeywordCall keyword) {

    // check if this instance is a inline keyword
    if (TestLangModelUtils.isInlineKeyword(keyword)) {
      return;
    }

    List<KeywordDecl> keywords = TestLangModelUtils.getAllKeywords(keyword);
    boolean keywordExists = false;
    for (KeywordDecl keywordDecl : keywords) {
      String name1 = TestLangModelUtils.normalizeKeyword(keyword.getName());
      String name2 = TestLangModelUtils.normalizeKeyword(keywordDecl.getName());
      if (name1.equals(name2)) {
        keywordExists = true;
      }
    }

    if (!keywordExists) {
      error("Keyword does not exists", TestLangPackage.Literals.KEYWORD_CALL__NAME, KEYWORD_NOT_EXISTS);
    }
  }

}
