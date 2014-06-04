package com.github.thomasfischl.kylang.test.ui.quickfix;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.github.thomasfischl.kylang.test.testLang.KeywordCall;
import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
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
        keywordImpl.getScript().setClass("ImplementMeClass");

        if (keywordCall.getParameters() != null && keywordCall.getParameters().getParameters() != null) {
          for (int i = 0; i < keywordCall.getParameters().getParameters().size(); i++) {
            PropertyDecl prop = TestLangFactory.eINSTANCE.createPropertyDecl();
            prop.setName("property" + (i + 1));
            keywordImpl.getProperties().add(prop);
          }
        }

        Model root = TestLangModelUtils.getRoot(keyword);
        root.getKeywords().add(keywordImpl);
      }
    }
  }

  @Fix(TestLangJavaValidator.KEYWORD_NOT_EXISTS)
  public void fixName(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Create scripted keyword", "Create a new scripted keyword.", "upcase.png", new CreateKeywordHandler());
  }

}
