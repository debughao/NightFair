package com.nightfair.mobille.util;

public class ErrorCodeUtil {

	public static ErrorCodeUtil mErrorCodeRegistLogin;
	
	public ErrorCodeUtil(){};
	
	public static ErrorCodeUtil getInstance(){
		if(mErrorCodeRegistLogin == null){
			mErrorCodeRegistLogin = new ErrorCodeUtil();
		}
		return mErrorCodeRegistLogin;
	}
	
	
	
	
	
}
