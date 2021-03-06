/*
 * generated by Xtext
 */
package com.github.thomasfischl.kylang.test.ui.contentassist;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.github.thomasfischl.kylang.test.testLang.KeywordDecl;
import com.github.thomasfischl.kylang.util.TestLangModelUtils;

/**
 * see http://www.eclipse.org/Xtext/documentation.html#contentAssist on how to customize content assistant
 */
public class TestLangProposalProvider extends com.github.thomasfischl.kylang.test.ui.contentassist.AbstractTestLangProposalProvider {

  @Override
  public void completeKeywordCall_Name(EObject model, Assignment assignment, ContentAssistContext context,
      ICompletionProposalAcceptor acceptor) {
    super.completeKeywordCall_Name(model, assignment, context, acceptor);

    List<KeywordDecl> keywords = TestLangModelUtils.getAllKeywords(model);
    KeywordDecl parentKeyword = TestLangModelUtils.getKeywordDecl(model);

    for (KeywordDecl keyword : keywords) {
      // ignore the current keyword from the list to avoid loops
      if (parentKeyword == keyword) {
        continue;
      }
      acceptor.accept(createCompletionProposal(keyword.getName(), context));
    }
  }
}
