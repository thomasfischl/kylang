package com.github.thomasfischl.kylang.test.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

import com.github.thomasfischl.kylang.test.services.TestLangGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#formatting on how and when to use it
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class TestLangFormatter extends AbstractDeclarativeFormatter {

  @Override
  protected void configureFormatting(final FormattingConfig c) {
    TestLangGrammarAccess f = (TestLangGrammarAccess) getGrammarAccess();

    c.setAutoLinewrap(140);

    // KeywordDecl
    c.setLinewrap().after(f.getKeywordDeclRule());

    c.setNoSpace().before(f.getKeywordDeclAccess().getLessThanSignKeyword_1_0());
    c.setNoSpace().after(f.getKeywordDeclAccess().getLessThanSignKeyword_1_0());
    c.setNoSpace().before(f.getKeywordDeclAccess().getGreaterThanSignKeyword_1_2());
    c.setSpace(" ").after(f.getKeywordDeclAccess().getGreaterThanSignKeyword_1_2());
    c.setNoSpace().after(f.getKeywordDeclAccess().getNameIDEXTTerminalRuleCall_2_0());

    c.setLinewrap(2).after(f.getKeywordDeclAccess().getPropertiesPropertyDeclParserRuleCall_5_0());
    c.setLinewrap(2).after(f.getKeywordDeclAccess().getDescDescriptionParserRuleCall_4_0());

    c.setLinewrap().after(f.getKeywordDeclAccess().getLeftCurlyBracketKeyword_3());
    c.setLinewrap(2).after(f.getKeywordDeclAccess().getRightCurlyBracketKeyword_7());

    c.setIndentationIncrement().after(f.getKeywordDeclAccess().getLeftCurlyBracketKeyword_3());
    c.setIndentationDecrement().before(f.getKeywordDeclAccess().getRightCurlyBracketKeyword_7());

    c.setLinewrap().after(f.getKeywordDeclAccess().getScriptAssignment_6_0());

    // PropertyDecl
    c.setLinewrap().after(f.getPropertyDeclRule());

    // KeywordCall
    c.setLinewrap().after(f.getKeywordCallRule());
    c.setIndentationIncrement().after(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setIndentationDecrement().before(f.getKeywordCallKeywordListAccess().getRightCurlyBracketKeyword_2());

    c.setNoSpace().before(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setLinewrap().after(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setLinewrap().after(f.getKeywordCallKeywordListAccess().getRightCurlyBracketKeyword_2());

  }
}
