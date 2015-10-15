package com.nightfair.mobille.util;

public class StringUtils {
	public static String[] convertStrToArry(String str) {
		String[] strArray = null;
			strArray = str.split("-");			
		return strArray;		
	}
	public static int getPosition(String str1,String[]str2){
		int n = 0;
		for (int i = 0; i < str2.length; i++) {
			if(str2[i].equals(str1)){
				n=i;
				break;
			}
		}
		return n;		
	}
}
