package com.flong.springboot.core.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class NamingStrategyUtils {

	/**
	 * 驼峰法转下划线
	 */
	public static String camelToUnderline(String line){
		if(line==null||"".equals(line)){
			return "";
		}
		line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(word.toLowerCase());
			sb.append(matcher.end()==line.length()?"":"_");
		}
		return sb.toString().toUpperCase();
	}
}
