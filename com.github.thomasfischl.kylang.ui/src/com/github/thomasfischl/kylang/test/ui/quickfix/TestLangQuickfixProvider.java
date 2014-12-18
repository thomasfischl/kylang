package com.github.thomasfischl.kylang.test.ui.quickfix;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.github.thomasfischl.kylang.test.testLang.Description;
import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.test.testLang.KeywordList;
import com.github.thomasfischl.kylang.test.testLang.KeywordMetatype;
import com.github.thomasfischl.kylang.test.testLang.Model;
import com.github.thomasfischl.kylang.test.testLang.PropertyDecl;
import com.github.thomasfischl.kylang.test.testLang.ScriptType;
import com.github.thomasfischl.kylang.test.testLang.TestLangFactory;
import com.github.thomasfischl.kylang.test.validation.TestLangJavaValidator;
import com.github.thomasfischl.kylang.util.TestLangModelUtils;

/**
 * Custom quickfixes.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#quickfixes
 */
public class TestLangQuickfixProvider extends DefaultQuickfixProvider {

  private final class CreateKeywordHandler implements ISemanticModification {
    @Override
    public void apply(EObject keyword, IModificationContext context) {
      if (keyword instanceof KeywordCall) {
        KeywordCall keywordCall = (KeywordCall) keyword;

        KeywordDecl keywordImpl = TestLangFactory.eINSTANCE.createKeywordDecl();
        keywordImpl.setName(keywordCall.getName());
        keywordImpl.setMetatype(KeywordMetatype.SCRIPTED);
        keywordImpl.setScript(TestLangFactory.eINSTANCE.createKeywordScript());
        keywordImpl.getScript().setScriptType(ScriptType.JAVA);
        keywordImpl.getScript().setClass("com.github.thomasfischl.kylang.runtime.keywords.impl.UnimplementedKeyword");

        Description description = TestLangFactory.eINSTANCE.createDescription();
        description.setText("'''TODO: Implement'''");
        keywordImpl.setDesc(description);

        defineParameters(keywordCall, keywordImpl);

        Model root = TestLangModelUtils.getRoot(keyword);
        root.getKeywords().add(keywordImpl);
      }
    }
  }

  private final class CreateUserKeywordHandler implements ISemanticModification {
    @Override
    public void apply(EObject keyword, IModificationContext context) {
      if (keyword instanceof KeywordCall) {
        KeywordCall keywordCall = (KeywordCall) keyword;

        KeywordDecl keywordImpl = TestLangFactory.eINSTANCE.createKeywordDecl();
        keywordImpl.setName(keywordCall.getName());
        keywordImpl.setMetatype(KeywordMetatype.USERDEFINED);

        Description description = TestLangFactory.eINSTANCE.createDescription();
        description.setText("'''TODO: Implement'''");
        keywordImpl.setDesc(description);

        KeywordList keywordList = TestLangFactory.eINSTANCE.createKeywordList();

        KeywordCall callKeyword = TestLangFactory.eINSTANCE.createKeywordCall();
        callKeyword.setName("Unimplemented Keyword");
        keywordList.getChildren().add(callKeyword);
        keywordImpl.setKeywordlist(keywordList);

        defineParameters(keywordCall, keywordImpl);

        Model root = TestLangModelUtils.getRoot(keyword);
        root.getKeywords().add(keywordImpl);
      }
    }

  }

  private void defineParameters(KeywordCall keywordCall, KeywordDecl keywordImpl) {
    if (keywordCall.getParameters() != null && keywordCall.getParameters().getParameters() != null) {
      for (int i = 0; i < keywordCall.getParameters().getParameters().size(); i++) {
        PropertyDecl prop = TestLangFactory.eINSTANCE.createPropertyDecl();
        prop.setName("property" + (i + 1));
        keywordImpl.getProperties().add(prop);
      }
    }
  }

  @Fix(TestLangJavaValidator.KEYWORD_NOT_EXISTS)
  public void fixName(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Create scripted keyword", "Create a new scripted keyword.", "upcase.png", new CreateKeywordHandler());
    acceptor.accept(issue, "Create user keyword", "Create a new user keyword.", "upcase.png", new CreateUserKeywordHandler());
  }

}
