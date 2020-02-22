package com.bestsch.zuoye.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum TimeType
{
	CURRENT_TERM(1, "本学期"), CURRENT_MONTH(2, "本月"), CURRENT_WEEK(3, "本周"),
	TODAY(4, "今日"),TOTAL(5,"累计");

	private int id;
	private String name;

	private TimeType(int id, String name)
	{
		setId(id);
		setName(name);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public static String nameById(int id)
	{
		for(TimeType type : list())
		{
			if(type.getId()==id) return type.getName();
		}
		return null;
		
	}
	
	public static Integer IdByName(String name)
	{
		for(TimeType type : list())
		{
			if(type.getName().equals(name)) return type.getId();
		}
		return null;
		
	}
	
	private static List<TimeType> list;
	public static List<TimeType> list()
	{
		if (list != null) return list;
		
		list = new ArrayList<>();
		
		EnumSet<TimeType> typeSet = EnumSet.allOf(TimeType.class);
		for (TimeType type : typeSet)
			list.add(type);
		
		return list;
	}
}
