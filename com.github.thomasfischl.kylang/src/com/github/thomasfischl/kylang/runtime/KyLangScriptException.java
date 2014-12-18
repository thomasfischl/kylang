package com.github.thomasfischl.kylang.runtime;

public class KyLangScriptException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public KyLangScriptException(String message, Throwable cause) {
    super(message, cause);
  }

  public KyLangScriptException(String message) {
    super(message);
  }

  public KyLangScriptException(Throwable cause) {
    super(cause);
  }

}
