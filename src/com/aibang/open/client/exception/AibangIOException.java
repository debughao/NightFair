package com.aibang.open.client.exception;

import java.io.IOException;

public class AibangIOException extends AibangException
{
  private static final long serialVersionUID = 1L;

  public AibangIOException(IOException reason)
  {
    super(reason);
  }
}