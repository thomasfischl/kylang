package com.github.thomasfischl.kylang.test.ui;

import static org.eclipse.swt.SWT.NORMAL;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class TestLangHighlightingConfiguration implements IHighlightingConfiguration, ISemanticHighlightingCalculator {

  // provide an id string for the highlighting calculator
  public static final String PARAM = "Parameter";
  public static final String PARAM_VAL = "Parameter Value";
  public static final String VARIABLE = "Variable";
  public static final String VARIABLE_VAL = "Variable Val";
  public static final String COMMENT = "Comment";
  public static final String STRING = "String";
  public static final String NUMBER = "Number";
  public static final String GROUP = "Group";
  public static final String PFT = "PFT";
  public static final String GROUP_NAME = "Group Name";
  public static final String[] ALL_STRINGS = { PARAM, PARAM_VAL, VARIABLE, VARIABLE_VAL, STRING, NUMBER, GROUP, PFT };

  // configure the acceptor providing the id, the description string
  // that will appear in the preference page and the initial text style
  @Override
  public void configure(IHighlightingConfigurationAcceptor acceptor) {
    addType(acceptor, PARAM, 50, 0, 0, NORMAL);
    addType(acceptor, PARAM_VAL, 50, 0, 0, NORMAL);
    addType(acceptor, VARIABLE, 50, 0, 0, NORMAL);
    addType(acceptor, VARIABLE_VAL, 50, 0, 0, NORMAL);
    addType(acceptor, STRING, 50, 0, 0, NORMAL);
    addType(acceptor, NUMBER, 50, 0, 0, NORMAL);
    addType(acceptor, GROUP, 50, 0, 0, NORMAL);
    addType(acceptor, PFT, 50, 0, 0, NORMAL);
    addType(acceptor, GROUP_NAME, 50, 0, 0, NORMAL);
    addType(acceptor, COMMENT, 150, 200, 200, NORMAL);
  }

  public void addType(IHighlightingConfigurationAcceptor acceptor, String s, int r, int g, int b, int style) {
    TextStyle textStyle = new TextStyle();
    textStyle.setBackgroundColor(new RGB(255, 255, 255));
    textStyle.setColor(new RGB(r, g, b));
    textStyle.setStyle(style);
    acceptor.acceptDefaultHighlighting(s, s, textStyle);
  }

  @Override
  public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
    // TODO Auto-generated method stub

  }

}
