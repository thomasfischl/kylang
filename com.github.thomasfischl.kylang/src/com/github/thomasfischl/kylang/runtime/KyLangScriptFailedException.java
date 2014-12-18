package com.github.thomasfischl.kylang.runtime;

public class KyLangScriptFailedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public KyLangScriptFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public KyLangScriptFailedException(String message) {
    super(message);
  }

  public KyLangScriptFailedException(Throwable cause) {
    super(cause);
  }

}
