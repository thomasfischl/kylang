package com.github.thomasfischl.kylang.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.test.testLang.Model;

public class TestLangModelUtils {

  public static List<KeywordDecl> getAllKeywords(EObject obj) {
    return getAllKeywords(getRoot(obj));
  }

  public static List<KeywordDecl> getAllKeywords(Model root) {
    List<KeywordDecl> keywords = new ArrayList<>();
    if (root != null) {
      for (KeywordDecl keyword : root.getKeywords()) {
        keywords.add(keyword);
      }
    }
    return keywords;
  }

  public static Model getRoot(EObject obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof Model) {
      return (Model) obj;
    }

    return getRoot(obj.eContainer());
  }

  public static String normalizeKeyword(String name) {
    if (name == null) {
      return null;
    }
    return name.trim();
  }

  public static KeywordDecl getKeywordDecl(EObject obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof KeywordDecl) {
      return (KeywordDecl) obj;
    }

    return getKeywordDecl(obj.eContainer());
  }

  public static boolean isInlineKeyword(KeywordCall keyword) {
    return !keyword.isHasParameters() && keyword.isHasKeywordList();
  }

}
