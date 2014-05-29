package com.github.thomasfischl.kylang.test;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class TestLangRuntimeModule extends com.github.thomasfischl.kylang.test.AbstractTestLangRuntimeModule {

  @Override
  public Class<? extends org.eclipse.xtext.formatting.IFormatter> bindIFormatter() {
    return com.github.thomasfischl.kylang.test.formatting.TestLangFormatter.class;
  }

}
