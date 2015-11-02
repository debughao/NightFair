package com.aibang.open.client.exception;

public class AibangException extends Exception
{
  private static final long serialVersionUID = 1L;

  public AibangException(String errMsg)
  {
    super(errMsg);
  }

  public AibangException(String errMsg, Throwable reason) {
    super(errMsg, reason);
  }

  public AibangException(Throwable reason) {
    super(reason);
  }
}