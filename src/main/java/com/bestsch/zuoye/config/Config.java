package com.bestsch.zuoye.config;

import java.util.ResourceBundle;

public enum Config
{
	Inst;
	
	public static final String API_PATH = "/api/";
	public static final String MOBILEAPI_PATH = "/mobiapi/";

	public String getString(String key)
	{
		ResourceBundle resource = ResourceBundle.getBundle("config");
		return resource.getString(key);
	}
	
	public int getInt(String key)
	{
		String value = getString(key);
		return Integer.parseInt(value);
	}

	}
