package com.aibang.open.client.exception;

public class AibangServerException extends AibangException
{
  private int statusCode;
  private static final long serialVersionUID = 1L;

  public AibangServerException(int statusCode, String errMsg)
  {
    super(errMsg);
    this.statusCode = statusCode;
  }

  public int getStatusCode()
  {
    return this.statusCode;
  }
}